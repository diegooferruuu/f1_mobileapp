package com.example.f1_app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.f1_app.features.home.presentation.HomeScreen
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.example.f1_app.features.calendar.presentation.CalendarScreen
import com.example.f1_app.features.drivers.presentation.DriversScreen
import com.example.f1_app.features.teams.presentation.TeamsScreen

@Composable
fun BottomNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") { HomeScreen() }
        composable("calendar") { CalendarScreen() }
        composable("teams") { TeamsScreen() }
        composable("drivers") { DriversScreen() }
        composable("results") { Text("FUNCIONALIDAD EN DESARROLLO") }
    }
}

