package com.example.f1_app.features.results.domain.repository

import com.example.f1_app.features.results.domain.model.Session
import com.example.f1_app.features.results.domain.model.SessionResult
import com.example.f1_app.features.results.domain.model.FastLap

interface ResultsRepository {
    suspend fun searchSessions(
        location: String? = null,
        year: Int? = null,
        sessionType: String? = null,
        sessionName: String? = null
    ): List<Session>

    suspend fun getSessionResults(sessionKey: Int): List<SessionResult>
    suspend fun getFastestLap(sessionKey: Int): FastLap?
}
