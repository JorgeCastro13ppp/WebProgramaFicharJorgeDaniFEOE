package com.empresa.adminpanel.models

import kotlinx.serialization.Serializable

@Serializable
data class RevisarHorasExtra(
    val estado: String,

    val comentario: String? = null
)
