package com.example.musicstreamingapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.musicstreamingapp.ui.navigation.LocalNavController
import com.example.musicstreamingapp.ui.navigation.Screen
import com.example.musicstreamingapp.util.Constants
import com.example.musicstreamingapp.util.FcmHelper
import com.example.musicstreamingapp.util.PreferencesManager
import kotlinx.coroutines.flow.StateFlow

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)
    val userId = preferencesManager.getUserId()
    val isFirstLaunch = preferencesManager.isFirstLaunch()
    val navController = LocalNavController.current
    if (isFirstLaunch) preferencesManager.setFirstLaunchStatus(false)
    var fcmToken by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        fcmToken = FcmHelper.getFcmToken(context)
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.Magenta),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Home Screen",
            modifier = Modifier.padding(16.dp)
        )

        Text(
            text = "User ID: $userId",
            modifier = Modifier.padding(16.dp)
        )

        Text(
            text = "Is First Launch: $isFirstLaunch",
            modifier = Modifier.padding(16.dp)
        )

        Text(
            text = "FCM Token: ${fcmToken ?: "Loading..."}",
            modifier = Modifier.padding(16.dp)
        )

        Button(
            onClick = {
                preferencesManager.setUserId(Constants.DEFAULT_USER_ID)
                preferencesManager.setFirstLaunchStatus(true)
                navController.navigate(Screen.Welcome.route) {
                    popUpTo("welcome") { inclusive = true }
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Log Out")
        }
    }
}