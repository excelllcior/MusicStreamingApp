package com.example.musicstreamingapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.musicstreamingapp.ui.navigation.LocalNavController
import com.example.musicstreamingapp.util.PreferencesManager

@Composable
fun RegistrationScreen() {
    val navController = LocalNavController.current
    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registration Screen",
            modifier = Modifier.padding(16.dp)
        )
        Button(
            onClick = {
                // Save user ID (100 for test) and navigate to home
                preferencesManager.setUserId(100)
                navController.navigate("home") {
                    popUpTo("welcome") { inclusive = true }
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Confirm Registration")
        }
    }
}