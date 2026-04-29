package com.empresa.adminpanel.models

import kotlinx.serialization.Serializable

@Serializable
data class JornadaPendiente(
    val id: Int,
    val userId: Int,
    val username: String,
    val fecha: String,
    val entradaReal: Long?,
    val salidaReal: Long?,
    val cerradaAutomaticamente: Boolean,
    val tiempoExtraDetectado: Long
)