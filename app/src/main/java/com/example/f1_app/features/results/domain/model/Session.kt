package com.example.f1_app.features.results.domain.model

import java.time.OffsetDateTime

data class Session(
    val meetingKey: Int,
    val sessionKey: Int,
    val location: String,
    val dateStart: OffsetDateTime,
    val dateEnd: OffsetDateTime?,
    val sessionType: String,
    val sessionName: String,
    val countryCode: String,
    val countryName: String,
    val circuitShortName: String,
    val year: Int
)
