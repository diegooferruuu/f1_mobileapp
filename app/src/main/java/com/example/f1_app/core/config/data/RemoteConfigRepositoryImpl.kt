package com.example.f1_app.core.config.data

import android.util.Log
import com.example.f1_app.core.config.domain.RemoteConfigRepository
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await

class RemoteConfigRepositoryImpl(
    private val remoteConfig: FirebaseRemoteConfig
) : RemoteConfigRepository {

    companion object {
        private const val TAG = "RemoteConfig"
    }

    init {
        // Configuración para desarrollo (fetch inmediato)
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0 // 0 para desarrollo, cambiar a 3600 en producción
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        // Valores por defecto
        remoteConfig.setDefaultsAsync(
            mapOf(
                "title" to "F1 Hub"
            )
        )

        Log.d(TAG, "Remote Config initialized with fetch interval: 0 seconds")
    }

    override suspend fun getString(key: String): String {
        val value = remoteConfig.getString(key)
        Log.d(TAG, "getString($key) = $value")
        return value
    }

    override suspend fun getBoolean(key: String): Boolean {
        val value = remoteConfig.getBoolean(key)
        Log.d(TAG, "getBoolean($key) = $value")
        return value
    }

    override suspend fun getLong(key: String): Long {
        val value = remoteConfig.getLong(key)
        Log.d(TAG, "getLong($key) = $value")
        return value
    }

    override suspend fun getDouble(key: String): Double {
        val value = remoteConfig.getDouble(key)
        Log.d(TAG, "getDouble($key) = $value")
        return value
    }

    override suspend fun fetchAndActivate(): Boolean {
        return try {
            Log.d(TAG, "Starting fetch and activate...")
            val success = remoteConfig.fetchAndActivate().await()
            Log.d(TAG, "Fetch and activate result: $success")

            // Log all current values
            val info = remoteConfig.info
            Log.d(TAG, "Config info - Last fetch status: ${info.lastFetchStatus}")
            Log.d(TAG, "Config info - Last fetch time: ${info.fetchTimeMillis}")

            success
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching Remote Config", e)
            false
        }
    }
}

