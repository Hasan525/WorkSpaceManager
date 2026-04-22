package com.workspace.manager.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.workspace.manager.data.model.AssetDto
import com.workspace.manager.data.model.NoteDto
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val notesCollection get() = firestore.collection("workspace").document("shared").collection("notes")
    private val assetsCollection get() = firestore.collection("workspace").document("shared").collection("assets")

    /** Sign in anonymously so every device gets a stable UID */
    suspend fun ensureAuth() {
        if (auth.currentUser == null) {
            auth.signInAnonymously().await()
        }
    }

    // ── Notes ─────────────────────────────────────────────────────────────────

    fun observeNotes(): Flow<List<NoteDto>> = callbackFlow {
        val registration = notesCollection.addSnapshotListener { snapshot, error ->
            if (error != null) { close(error); return@addSnapshotListener }
            val dtos = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(NoteDto::class.java)?.copy(id = doc.id)
            } ?: emptyList()
            trySend(dtos)
        }
        awaitClose { registration.remove() }
    }

    suspend fun saveNote(dto: NoteDto) {
        notesCollection.document(dto.id).set(dto, SetOptions.merge()).await()
    }

    suspend fun deleteNote(id: String) {
        notesCollection.document(id).delete().await()
    }

    // ── Assets ────────────────────────────────────────────────────────────────

    fun observeAssets(): Flow<List<AssetDto>> = callbackFlow {
        val registration = assetsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) { close(error); return@addSnapshotListener }
            val dtos = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(AssetDto::class.java)?.copy(id = doc.id)
            } ?: emptyList()
            trySend(dtos)
        }
        awaitClose { registration.remove() }
    }

    suspend fun saveAsset(dto: AssetDto) {
        assetsCollection.document(dto.id).set(dto, SetOptions.merge()).await()
    }

    suspend fun deleteAsset(id: String) {
        assetsCollection.document(id).delete().await()
    }
}
