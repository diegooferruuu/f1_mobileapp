package com.example.f1_app.features.calendar.data.api.dto

import com.squareup.moshi.Json

data class MeetingDto(
    @Json(name = "meeting_key") val meetingKey: Int,
    @Json(name = "circuit_short_name") val circuitShortName: String,
    @Json(name = "meeting_code") val meetingCode: String,
    @Json(name = "location") val location: String,
    @Json(name = "country_code") val countryCode: String,
    @Json(name = "country_name") val countryName: String,
    @Json(name = "meeting_name") val meetingName: String,
    @Json(name = "date_start") val dateStart: String,
    @Json(name = "year") val year: Int
)
