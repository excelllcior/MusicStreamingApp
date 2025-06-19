package com.example.musicstreamingapp.util

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class FirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Refreshed token: $token")
    }
}