package com.example.f1_app.features.home.framework

import android.util.Log
import com.example.f1_app.features.home.data.datasource.HomeRemoteDataSource
import com.example.f1_app.features.home.domain.model.HomeOverview
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseHomeDataSource(
    private val database: FirebaseDatabase
) : HomeRemoteDataSource {

    companion object {
        private const val TAG = "FirebaseHomeDataSource"
        private const val OVERVIEW_PATH = "overview"
    }

    override suspend fun getOverview(): HomeOverview = suspendCancellableCoroutine { continuation ->
        val reference = database.getReference(OVERVIEW_PATH)

        Log.d(TAG, "Fetching overview from path: $OVERVIEW_PATH")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    Log.d(TAG, "Data received: ${snapshot.value}")

                    // Extract data from snapshot according to the structure shown in the image
                    val racesStr = snapshot.child("races").value?.toString() ?: "0"
                    val races = racesStr.toIntOrNull() ?: 0

                    val podiums = snapshot.child("podiums").value?.toString()?.toIntOrNull() ?: 0
                    val safetyCars = snapshot.child("sc").value?.toString()?.toIntOrNull() ?: 0
                    val nextRace = snapshot.child("next").value?.toString() ?: ""
                    val weather = snapshot.child("weather").value?.toString() ?: ""
                    val firstPractice = snapshot.child("fp1").value?.toString() ?: ""

                    val overview = HomeOverview(
                        racesCompleted = races,
                        driversOnPodium = podiums,
                        safetyCars = safetyCars,
                        nextRace = nextRace,
                        weather = weather,
                        firstPractice = firstPractice
                    )

                    Log.d(TAG, "Overview parsed: $overview")
                    continuation.resume(overview)
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing overview data", e)
                    continuation.resumeWithException(e)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Database error: ${error.message}")
                continuation.resumeWithException(error.toException())
            }
        }

        reference.addListenerForSingleValueEvent(listener)

        continuation.invokeOnCancellation {
            reference.removeEventListener(listener)
        }
    }
}

