package com.empresa.adminpanel.models

import kotlinx.serialization.Serializable

@Serializable
data class Fichaje(
    val id: Int,
    val userId: Int,
    val username: String,
    val fechaHora: Long,
    val tipo: String
)