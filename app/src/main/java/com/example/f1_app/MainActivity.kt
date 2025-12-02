package com.example.f1_app
import android.Manifest
import android.os.Build
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.f1_app.features.notifications.domain.NotificationScheduler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.f1_app.features.auth.presentation.LoginScreen
import com.example.f1_app.features.auth.presentation.SignUpScreen
import com.example.f1_app.navigation.BottomNavBar
import com.example.f1_app.navigation.BottomNavGraph
import com.example.f1_app.navigation.BottomNavItem
import com.example.f1_app.presentation.MainViewModel
import com.example.f1_app.ui.theme.F1_appTheme
import org.koin.androidx.compose.koinViewModel


class MainActivity : ComponentActivity() {
        private val requestNotificationPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    NotificationScheduler.scheduleImmediate(
                        this,
                        title = "Bienvenido a F1",
                        body = "Explora los últimos resultados"
                    )
                    // Start periodic 15-minute reminders once permission is granted
                    NotificationScheduler.schedulePeriodicReminder(this, 15)
                }
            }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            F1_appTheme {
                AppNavigationHost()
            }
        }

        // Auto notification on app launch
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (hasPermission) {
                NotificationScheduler.scheduleImmediate(
                    this,
                    title = "Bienvenido a F1",
                    body = "Explora los últimos resultados"
                )
                NotificationScheduler.schedulePeriodicReminder(this, 15)
            } else {
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            NotificationScheduler.scheduleImmediate(
                this,
                title = "Bienvenido a F1",
                body = "Explora los últimos resultados"
            )
            NotificationScheduler.schedulePeriodicReminder(this, 15)
        }
    }
}

@Composable
fun AppNavigationHost() {
    val navController = rememberNavController()
    val viewModel: MainViewModel = koinViewModel()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsStateWithLifecycle()

    // Show loading while checking auth state
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // Determine start destination based on auth state
    val startDestination = if (isUserLoggedIn) "main" else "login"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate("signup")
                },
                onSkip = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("signup") {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onSkip = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("main") {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem("home", "Home", Icons.Default.Home),
        BottomNavItem("calendar", "Calendar", Icons.Default.DateRange),
        BottomNavItem("teams", "Teams", Icons.Default.Group),
        BottomNavItem("drivers", "Drivers", Icons.Default.Person),
        BottomNavItem("results", "Results", Icons.Default.Flag)
    )

    Scaffold(
        bottomBar = { BottomNavBar(navController, items) }
    ) { innerPadding ->
        BottomNavGraph(navController, modifier = Modifier.padding(innerPadding))
    }

}