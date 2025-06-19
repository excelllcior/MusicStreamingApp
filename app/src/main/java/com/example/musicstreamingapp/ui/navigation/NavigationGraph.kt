package com.example.musicstreamingapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.musicstreamingapp.ui.AppScreen
import com.example.musicstreamingapp.ui.screen.HomeScreen
import com.example.musicstreamingapp.ui.screen.RegistrationScreen
import com.example.musicstreamingapp.ui.screen.WelcomeScreen
import com.example.musicstreamingapp.util.PreferencesManager

@Composable
fun NavGraph(navController: NavHostController) {
    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)

    val startDestination = if (preferencesManager.isUserRegistered()) {
        Screen.Home.route
    }
    else {
        Screen.Welcome.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Welcome
        composable(Screen.Welcome.route) {
            WelcomeScreen()
        }

        // Registration
        composable(Screen.Registration.route) {
            RegistrationScreen()
        }

        // Home
        composable(Screen.Home.route) {
            AppScreen {
                HomeScreen()
            }
        }
    }
}