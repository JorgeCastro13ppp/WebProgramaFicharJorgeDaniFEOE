package com.empresa.adminpanel.models

import kotlinx.serialization.Serializable

@Serializable
data class Falta(
    val id: Int,
    val userId: Int,
    val username: String,
    val fecha: String,
    val tipo: String,
    val descripcion: String
)