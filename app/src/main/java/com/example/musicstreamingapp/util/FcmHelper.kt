package com.example.musicstreamingapp.util

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

object FcmHelper {
    private var isInitialized = false

    fun initialize(context: Context) {
        try {
            FirebaseApp.initializeApp(context)
            isInitialized = true
        } catch (e: IllegalStateException) {
            // Already initialized
            isInitialized = true
        }
    }

    suspend fun getFcmToken(context: Context): String? {
        if (!isInitialized) {
            initialize(context)
        }

        return try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            Log.e("FCM Helper", "Failed to get FCM Token", e)
            null
        }
    }
}