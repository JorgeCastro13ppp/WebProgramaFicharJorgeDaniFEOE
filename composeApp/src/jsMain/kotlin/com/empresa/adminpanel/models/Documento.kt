package com.empresa.adminpanel.models

import kotlinx.serialization.Serializable

@Serializable
data class Documento(
    val id: Int,
    val userId: Int,
    val nombre: String,
    val tipo: String,
    val url: String
)
