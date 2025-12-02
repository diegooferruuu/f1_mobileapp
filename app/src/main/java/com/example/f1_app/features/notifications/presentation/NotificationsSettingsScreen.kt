package com.example.f1_app.features.notifications.presentation

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.f1_app.features.notifications.domain.NotificationScheduler

@Composable
fun NotificationsSettingsScreen(contentPadding: PaddingValues) {
    val context = LocalContext.current
    val enabled = remember { mutableStateOf(true) }

    Column {
        Text(text = "Notificaciones internas")
        Spacer(Modifier.height(12.dp))
        Switch(checked = enabled.value, onCheckedChange = { enabled.value = it })
        Spacer(Modifier.height(12.dp))
        Button(onClick = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Permission should be requested from an Activity; just an example
            }
            NotificationScheduler.scheduleImmediate(
                context,
                title = "Nueva sesión disponible",
                body = "Abre para ver resultados"
            )
        }) { Text("Probar notificación") }
    }
}
