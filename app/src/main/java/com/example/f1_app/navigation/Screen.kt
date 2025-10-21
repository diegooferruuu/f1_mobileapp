package com.example.f1_app.navigation

sealed class Screen(val route: String) {

    object HomeScreen: Screen("home")
    object Calendar: Screen("calendar")
    object Profile: Screen("profile")
    object LoginScreen: Screen("login")
}