package com.example.f1_app.features.results.data.api.dto

import com.squareup.moshi.Json

data class LapDto(
    @Json(name = "meeting_key") val meetingKey: Int,
    @Json(name = "session_key") val sessionKey: Int,
    @Json(name = "driver_number") val driverNumber: Int,
    @Json(name = "lap_number") val lapNumber: Int,
    @Json(name = "duration_sector_1") val durationSector1: Double?,
    @Json(name = "duration_sector_2") val durationSector2: Double?,
    @Json(name = "duration_sector_3") val durationSector3: Double?,
    @Json(name = "lap_duration") val lapDuration: Double?
)
