package com.example.f1_app.features.calendar.domain.model

import java.time.OffsetDateTime

data class Meeting(
    val meetingKey: Int,
    val circuitShortName: String,
    val meetingCode: String,
    val location: String,
    val countryCode: String,
    val countryName: String,
    val meetingName: String,
    val dateStart: OffsetDateTime,
    val year: Int
)
