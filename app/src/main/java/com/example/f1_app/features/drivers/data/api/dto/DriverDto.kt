package com.example.f1_app.features.drivers.data.api.dto

import com.squareup.moshi.Json
data class TeamsResponseWrapper(
    @Json(name = "response") val response: List<DriverDto>
)

data class DriverDto(
    @Json(name = "id") val id: Int?,
    @Json(name = "full_name") val name: String?,
    @Json(name = "headshot_url") val image: String?,
    @Json(name = "nationality") val nationality: String?,
    @Json(name = "name_acronym") val abbr: String?,
    @Json(name = "driver_number") val number: Int?
)
