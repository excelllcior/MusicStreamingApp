package com.example.musicstreamingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.rememberNavController
import com.example.musicstreamingapp.ui.navigation.LocalNavController
import com.example.musicstreamingapp.ui.navigation.NavGraph
import com.example.musicstreamingapp.ui.theme.MusicStreamingAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MusicStreamingAppTheme(
                dynamicColor = false
            ) {
                val navController = rememberNavController()

                CompositionLocalProvider(
                    LocalNavController provides navController
                ) {
                    NavGraph(navController)
                }
            }
        }
    }
}