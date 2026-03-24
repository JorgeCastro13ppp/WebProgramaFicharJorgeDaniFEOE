package com.empresa.adminpanel.models

import kotlinx.serialization.Serializable

@Serializable
data class Vacacion(
    val id: Int,
    val userId: Int,
    val fechaInicio: String,
    val fechaFin: String,
    val estado: String
)