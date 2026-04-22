package com.workspace.manager.data.remote

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseStorageDataSource @Inject constructor(
    private val storage: FirebaseStorage
) {
    /**
     * Uploads an image from a local Uri to Firebase Storage.
     * Returns the public download URL.
     */
    suspend fun uploadImage(localUri: Uri, assetId: String): String {
        val ref = storage.reference.child("workspace/assets/$assetId.jpg")
        ref.putFile(localUri).await()
        return ref.downloadUrl.await().toString()
    }

    suspend fun deleteImage(assetId: String) {
        try {
            storage.reference.child("workspace/assets/$assetId.jpg").delete().await()
        } catch (e: Exception) {
            // File may not exist remotely yet — safe to ignore
        }
    }
}
