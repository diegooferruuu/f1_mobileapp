package com.example.f1_app.features.results.data.datasource

import com.example.f1_app.features.results.data.api.ResultsApiService
import com.example.f1_app.features.results.domain.model.Session
import com.example.f1_app.features.results.domain.model.SessionResult
import com.example.f1_app.features.results.domain.model.FastLap
import java.time.OffsetDateTime

class ResultsRemoteDataSource(private val api: ResultsApiService) {
    suspend fun searchSessions(location: String?, year: Int?, sessionType: String?, sessionName: String?): List<Session> =
        api.getSessions(location, year, sessionType, sessionName).map { dto ->
            Session(
                meetingKey = dto.meetingKey,
                sessionKey = dto.sessionKey,
                location = dto.location,
                dateStart = OffsetDateTime.parse(dto.dateStart),
                dateEnd = dto.dateEnd?.let { OffsetDateTime.parse(it) },
                sessionType = dto.sessionType,
                sessionName = dto.sessionName,
                countryCode = dto.countryCode,
                countryName = dto.countryName,
                circuitShortName = dto.circuitShortName,
                year = dto.year
            )
        }

    suspend fun getSessionResults(sessionKey: Int): List<SessionResult> =
        api.getSessionResults(sessionKey)
            .filter { it.position != null }
            .map { dto ->
                val gap = when (val raw = dto.gapToLeader) {
                    is String -> raw.trim().removePrefix("+").toDoubleOrNull()
                    is List<*> -> raw.filterIsInstance<Number>()
                        .map { it.toDouble() }
                        .lastOrNull() // take last segment (Q3) when available
                    else -> null
                }
                SessionResult(
                    position = dto.position,
                    driverNumber = dto.driverNumber,
                    numberOfLaps = dto.numberOfLaps,
                    points = dto.points,
                    dnf = dto.dnf,
                    dns = dto.dns,
                    dsq = dto.dsq,
                    durationSeconds = null,
                    gapToLeaderSeconds = gap,
                    meetingKey = dto.meetingKey,
                    sessionKey = dto.sessionKey
                )
            }

    suspend fun getFastestLap(sessionKey: Int): FastLap? {
        val laps = api.getLaps(sessionKey)
        // choose shortest valid lap (use lapDuration if present else sectors)
        val fastest = laps.mapNotNull { l ->
            val total = when {
                l.lapDuration != null -> l.lapDuration
                l.durationSector1 != null && l.durationSector2 != null && l.durationSector3 != null ->
                    l.durationSector1 + l.durationSector2 + l.durationSector3
                else -> null
            }
            total?.let { FastLap(driverNumber = l.driverNumber, lapNumber = l.lapNumber, totalTimeSeconds = it) }
        }.minByOrNull { it.totalTimeSeconds }
        return fastest
    }
}
