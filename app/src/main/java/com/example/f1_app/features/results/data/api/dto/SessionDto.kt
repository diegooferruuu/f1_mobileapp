package com.example.f1_app.features.results.data.api.dto

import com.squareup.moshi.Json

data class SessionDto(
    @Json(name = "meeting_key") val meetingKey: Int,
    @Json(name = "session_key") val sessionKey: Int,
    @Json(name = "location") val location: String,
    @Json(name = "date_start") val dateStart: String,
    @Json(name = "date_end") val dateEnd: String?,
    @Json(name = "session_type") val sessionType: String,
    @Json(name = "session_name") val sessionName: String,
    @Json(name = "country_code") val countryCode: String,
    @Json(name = "country_name") val countryName: String,
    @Json(name = "circuit_short_name") val circuitShortName: String,
    @Json(name = "year") val year: Int
)
