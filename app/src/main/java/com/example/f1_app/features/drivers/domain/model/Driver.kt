package com.example.f1_app.features.drivers.domain.model

data class Driver (
    val id: Int? = 0,
    val name: String? = "Sin nombre",
    val image: String? = "Sin imagen",
    val nationality: String? = "Sin nacionalidad",
    val abbr: String?= "Sin abreviacion",
    val number: Int?= 0
)