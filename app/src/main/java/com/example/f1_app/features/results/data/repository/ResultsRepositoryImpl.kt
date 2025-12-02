package com.example.f1_app.features.results.data.repository

import com.example.f1_app.features.results.data.datasource.ResultsRemoteDataSource
import com.example.f1_app.features.results.domain.model.Session
import com.example.f1_app.features.results.domain.model.SessionResult
import com.example.f1_app.features.results.domain.model.FastLap
import com.example.f1_app.features.results.domain.repository.ResultsRepository

class ResultsRepositoryImpl(private val remote: ResultsRemoteDataSource) : ResultsRepository {
    override suspend fun searchSessions(location: String?, year: Int?, sessionType: String?, sessionName: String?): List<Session> =
        remote.searchSessions(location, year, sessionType, sessionName)

    override suspend fun getSessionResults(sessionKey: Int): List<SessionResult> =
        remote.getSessionResults(sessionKey)

    override suspend fun getFastestLap(sessionKey: Int): FastLap? = remote.getFastestLap(sessionKey)
}
