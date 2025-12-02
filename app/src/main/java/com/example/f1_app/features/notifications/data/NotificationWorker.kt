package com.example.f1_app.features.notifications.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.f1_app.MainActivity
import com.example.f1_app.R

class NotificationWorker(
    appContext: Context,
    params: WorkerParameters
) : Worker(appContext, params) {

    override fun doWork(): Result {
        val title = inputData.getString(KEY_TITLE) ?: "Resultados F1"
        val body = inputData.getString(KEY_BODY) ?: "Revisa los últimos resultados"
        val sessionKey = inputData.getInt(KEY_SESSION, -1)

        ensureChannel()

        val args = Bundle().apply { if (sessionKey > 0) putInt("session_key", sessionKey) }
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            putExtras(args)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            (PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        )

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setColor(Color.RED)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                NotificationManagerCompat.from(applicationContext).notify(NOTIF_ID, notification)
            } catch (se: SecurityException) {
                // Permission revoked or restricted at runtime; safely ignore.
            }
        }
        return Result.success()
    }

    private fun ensureChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (nm.getNotificationChannel(CHANNEL_ID) == null) {
                val ch = NotificationChannel(
                    CHANNEL_ID,
                    "Resultados F1",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                ch.description = "Avisos internos de carreras y clasificación"
                nm.createNotificationChannel(ch)
            }
        }
    }

    companion object {
        const val CHANNEL_ID = "f1_results_channel"
        const val NOTIF_ID = 1001
        const val KEY_TITLE = "title"
        const val KEY_BODY = "body"
        const val KEY_SESSION = "session_key"
    }
}
