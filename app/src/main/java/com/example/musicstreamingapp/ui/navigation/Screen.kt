package com.example.musicstreamingapp.ui.navigation

sealed class Screen(val route: String) {
    /* ARGS */
    companion object {
        const val ID_ARG = "id"
    }

    object Home : Screen("home")
    object Welcome : Screen("welcome")
    object Registration : Screen("registration")
}
