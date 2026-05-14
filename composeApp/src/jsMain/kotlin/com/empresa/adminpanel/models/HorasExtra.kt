package com.empresa.adminpanel.models

import kotlinx.serialization.Serializable

@Serializable
data class HorasExtra(
    val id: Int,
    val userId: Int,
    val username: String,
    val fecha: String,
    val minutosExtra: Int,
    val estado: String,
    val aprobadoPor: Int? = null,
    val aprobadoPorUsername: String? = null,
    val fechaRevision: Long? = null,
    val comentario: String? = null
)