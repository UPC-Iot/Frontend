package com.example.protectify.data.repository.profile

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class CloudStorageRepository {

    private val storage = Firebase.storage
    private val storageRef = storage.reference

    private fun getStorageReference() = storageRef.child("photos")

    suspend fun uploadFile(filename: String, path: Uri): String {
        val file = getStorageReference().child(filename)
        file.putFile(path).await()
        return file.downloadUrl.await().toString()
    }
}