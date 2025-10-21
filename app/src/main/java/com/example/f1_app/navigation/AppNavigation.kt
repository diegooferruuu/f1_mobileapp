package com.example.f1_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.f1_app.features.home.presentation.HomeScreen
import com.example.f1_app.features.calendar.presentation.CalendarScreen


@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Calendar.route
    ) {
        composable(Screen.HomeScreen.route) {
            HomeScreen()
        }
        composable(Screen.Calendar.route) {
            CalendarScreen()
        }
        composable(Screen.LoginScreen.route){

        }
        composable(Screen.Profile.route) {

        }
    }
}