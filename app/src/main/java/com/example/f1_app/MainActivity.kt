package com.example.f1_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.Group
import androidx.compose.ui.tooling.data.Group
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.f1_app.navigation.AppNavigation
import com.example.f1_app.navigation.BottomNavBar
import com.example.f1_app.navigation.BottomNavGraph
import com.example.f1_app.navigation.BottomNavItem
import com.example.f1_app.ui.theme.F1_appTheme
import org.chromium.base.Flag


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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