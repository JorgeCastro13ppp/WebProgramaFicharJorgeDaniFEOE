package com.empresa.adminpanel.models

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val id: Int,
    val username: String,
    val role: String
)