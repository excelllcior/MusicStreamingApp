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
import androidx.compose.ui.unit.dp
import com.example.musicstreamingapp.ui.navigation.LocalNavController

@Composable
fun WelcomeScreen() {
    val navController = LocalNavController.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome Screen",
            modifier = Modifier.padding(16.dp)
        )

        Button(
            onClick = { navController.navigate("registration") },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Register")
        }
    }
}