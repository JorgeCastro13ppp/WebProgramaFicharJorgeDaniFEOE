package com.empresa.adminpanel.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String
)