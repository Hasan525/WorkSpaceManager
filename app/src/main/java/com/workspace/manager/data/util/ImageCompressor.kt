package com.workspace.manager.data.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Base64
import androidx.exifinterface.media.ExifInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max
import kotlin.math.min

/**
 * Loads an image from a content Uri, downscales it, applies EXIF orientation,
 * JPEG-compresses it, and base64-encodes the result so it can be stored as a
 * single string field in a Firestore document.
 *
 * Firestore caps each document at 1 MiB. Base64 inflates binary by ~33%, so the
 * compressed JPEG must stay under ~750 KB. We try a sequence of (maxEdge, quality)
 * settings and return the first one that fits.
 */
@Singleton
class ImageCompressor @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /** Returns base64-encoded JPEG bytes, ready to drop into a Firestore string field. */
    suspend fun compressToBase64(uri: Uri): String = withContext(Dispatchers.IO) {
        val raw = decodeAndOrient(uri)
            ?: throw IOException("Could not decode image from $uri")
        try {
            for (s in COMPRESSION_LADDER) {
                val resized = resize(raw, s.maxEdge)
                val bytes = jpeg(resized, s.quality)
                if (resized !== raw) resized.recycle()
                if (bytes.size <= MAX_BINARY_BYTES) {
                    return@withContext Base64.encodeToString(bytes, Base64.NO_WRAP)
                }
            }
            throw IOException(
                "Image is too large to store in Firestore. " +
                    "Try a smaller picture (under ~3 MP)."
            )
        } finally {
            raw.recycle()
        }
    }

    /** Decode the bitmap with sample-size optimisation and apply EXIF rotation. */
    private fun decodeAndOrient(uri: Uri): Bitmap? {
        val resolver = context.contentResolver

        // Pass 1 — read bounds only so we can pick a sane sample size
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null

        val sample = computeSampleSize(bounds.outWidth, bounds.outHeight, MAX_DECODE_EDGE)

        // Pass 2 — actual decode
        val opts = BitmapFactory.Options().apply { inSampleSize = sample }
        val decoded = resolver.openInputStream(uri)
            ?.use { BitmapFactory.decodeStream(it, null, opts) }
            ?: return null

        // EXIF orientation — phones routinely store landscape photos with a rotation flag
        val rotation = resolver.openInputStream(uri)?.use { stream ->
            try { ExifInterface(stream).rotationDegrees } catch (_: Exception) { 0 }
        } ?: 0

        return if (rotation != 0) rotate(decoded, rotation.toFloat()) else decoded
    }

    private fun resize(src: Bitmap, maxEdge: Int): Bitmap {
        val longest = max(src.width, src.height)
        if (longest <= maxEdge) return src
        val scale = maxEdge.toFloat() / longest
        val w = (src.width * scale).toInt().coerceAtLeast(1)
        val h = (src.height * scale).toInt().coerceAtLeast(1)
        return Bitmap.createScaledBitmap(src, w, h, true)
    }

    private fun rotate(src: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        val rotated = Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
        if (rotated !== src) src.recycle()
        return rotated
    }

    private fun jpeg(bitmap: Bitmap, quality: Int): ByteArray {
        val out = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
        return out.toByteArray()
    }

    private fun computeSampleSize(width: Int, height: Int, target: Int): Int {
        var sample = 1
        var w = width
        var h = height
        while (min(w, h) / 2 >= target) {
            w /= 2; h /= 2; sample *= 2
        }
        return sample
    }

    companion object {
        // ~750 KB binary → ~1000 KB base64; comfortably under Firestore's 1 MiB doc cap
        private const val MAX_BINARY_BYTES = 750_000
        // Don't bother decoding at full sensor resolution — saves memory on very large source files
        private const val MAX_DECODE_EDGE = 2048

        private data class Setting(val maxEdge: Int, val quality: Int)
        private val COMPRESSION_LADDER = listOf(
            Setting(maxEdge = 1280, quality = 75),
            Setting(maxEdge = 1024, quality = 70),
            Setting(maxEdge = 1024, quality = 55),
            Setting(maxEdge = 800,  quality = 60),
            Setting(maxEdge = 640,  quality = 55),
            Setting(maxEdge = 512,  quality = 50)
        )
    }
}
