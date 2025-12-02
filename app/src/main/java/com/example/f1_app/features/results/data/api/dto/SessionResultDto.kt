package com.example.f1_app.features.results.data.api.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class SessionResultDto(
    @Json(name = "position") val position: Int?,
    @Json(name = "driver_number") val driverNumber: Int,
    @Json(name = "number_of_laps") val numberOfLaps: Int?,
    @Json(name = "points") val points: Int?,
    @Json(name = "dnf") val dnf: Boolean,
    @Json(name = "dns") val dns: Boolean,
    @Json(name = "dsq") val dsq: Boolean,
    // Omit duration entirely; API returns either double or array depending on session
    @Json(name = "gap_to_leader") val gapToLeader: Any?,
    @Json(name = "meeting_key") val meetingKey: Int,
    @Json(name = "session_key") val sessionKey: Int
)
