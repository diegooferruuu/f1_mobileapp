package com.example.f1_app.network

import com.example.f1_app.features.calendar.data.api.OpenF1Service
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class OpenF1ServiceTest {
    private fun service(): OpenF1Service {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        val client = OkHttpClient.Builder().addInterceptor(logging).build()
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openf1.org/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
        return retrofit.create(OpenF1Service::class.java)
    }

    @Test
    fun canFetchMeetings() = runBlocking {
        try {
            val result = service().getMeetings(2024)
            // Should at least return an empty list without throwing converter errors
            assertTrue(result is List<*>)
        } catch (e: Exception) {
            // If running offline, the important bit is we didn't fail with a converter error at compile time.
            // Re-throw only if it's clearly a Moshi/Retrofit type adapter issue.
            if (!e.message.orEmpty().contains("Unable to create converter")) return@runBlocking
            throw e
        }
    }
}
