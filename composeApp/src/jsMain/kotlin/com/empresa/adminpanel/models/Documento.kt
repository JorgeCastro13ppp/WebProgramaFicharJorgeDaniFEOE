package com.empresa.adminpanel.models

import kotlinx.serialization.Serializable

@Serializable
data class Documento(
    val id: Int,
    val userId: Int,
    val username: String,
    val nombre: String,
    val tipo: String,
    val url: String
)
