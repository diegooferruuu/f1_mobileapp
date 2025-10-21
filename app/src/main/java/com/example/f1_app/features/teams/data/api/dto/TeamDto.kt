package com.example.f1_app.features.teams.data.api.dto

import com.squareup.moshi.Json
data class TeamsResponseWrapper(
    @Json(name = "response") val response: List<TeamDto>
)

data class TeamDto(
    @Json(name = "id") val id: Int?,
    @Json(name = "name") val name: String?,
    @Json(name = "logo") val logo: String?,
    @Json(name = "base") val base: String?,
    @Json(name = "director") val director: String?
)
