package com.example.f1_app.features.results.domain.model

data class SessionResult(
    val position: Int?,
    val driverNumber: Int,
    val numberOfLaps: Int?,
    val points: Int?,
    val dnf: Boolean,
    val dns: Boolean,
    val dsq: Boolean,
    val durationSeconds: Double?,
    val gapToLeaderSeconds: Double?,
    val meetingKey: Int,
    val sessionKey: Int
)
