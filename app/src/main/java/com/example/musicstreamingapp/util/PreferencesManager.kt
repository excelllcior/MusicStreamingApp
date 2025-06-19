package com.example.musicstreamingapp.util

import android.content.Context

class PreferencesManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(
        "App Preferences",
        Context.MODE_PRIVATE
    )

    fun setUserId(userId: Int) {
        sharedPreferences.edit().apply {
            putInt(
                Constants.KEY_USER_ID,
                userId
            )
            apply()
        }
    }

    fun setFirstLaunchStatus(isFirstLaunch: Boolean) {
        sharedPreferences.edit().apply {
            putBoolean(
                Constants.KEY_IS_FIRST_LAUNCH,
                isFirstLaunch
            )
            apply()
        }
    }

    fun getUserId(): Int {
        return sharedPreferences.getInt(
            Constants.KEY_USER_ID,
            Constants.DEFAULT_USER_ID
        )
    }

    fun isUserRegistered(): Boolean {
        return getUserId() != Constants.DEFAULT_USER_ID
    }

    fun isFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean(
            Constants.KEY_IS_FIRST_LAUNCH,
            true
        )
    }
}