package com.example.f1_app.features.notifications.domain

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.f1_app.features.notifications.data.NotificationWorker
import java.util.concurrent.TimeUnit

object NotificationScheduler {
    fun scheduleImmediate(
        context: Context,
        title: String,
        body: String,
        sessionKey: Int? = null
    ) {
        val data = workDataOf(
            NotificationWorker.KEY_TITLE to title,
            NotificationWorker.KEY_BODY to body,
            NotificationWorker.KEY_SESSION to (sessionKey ?: -1)
        )
        val req = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInputData(data)
            .build()
        WorkManager.getInstance(context).enqueue(req)
    }

    fun scheduleDailyReminder(
        context: Context,
        hour: Int = 9,
        minute: Int = 0
    ) {
        val now = System.currentTimeMillis()
        val cal = java.util.Calendar.getInstance().apply {
            timeInMillis = now
            set(java.util.Calendar.HOUR_OF_DAY, hour)
            set(java.util.Calendar.MINUTE, minute)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }
        var delay = cal.timeInMillis - now
        if (delay <= 0) delay += TimeUnit.DAYS.toMillis(1)

        val req = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf(
                    NotificationWorker.KEY_TITLE to "Recordatorio F1",
                    NotificationWorker.KEY_BODY to "Revisa las sesiones de hoy"
                )
            )
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork("daily_f1_reminder", ExistingWorkPolicy.REPLACE, req)
    }

    fun schedulePeriodicReminder(
        context: Context,
        intervalMinutes: Long = 15
    ) {
        val req = PeriodicWorkRequestBuilder<NotificationWorker>(
            intervalMinutes, TimeUnit.MINUTES
        ).setInputData(
            workDataOf(
                NotificationWorker.KEY_TITLE to "Recordatorio F1",
                NotificationWorker.KEY_BODY to "Revisa las sesiones recientes"
            )
        ).build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                "periodic_f1_reminder",
                ExistingPeriodicWorkPolicy.UPDATE,
                req
            )
    }

    fun cancelPeriodicReminder(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork("periodic_f1_reminder")
    }
}
