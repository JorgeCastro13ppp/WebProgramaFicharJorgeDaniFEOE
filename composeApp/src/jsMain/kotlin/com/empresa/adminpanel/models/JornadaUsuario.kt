package com.empresa.adminpanel.models

import kotlinx.serialization.Serializable

@Serializable
data class JornadaUsuario(

    val id: Int,

    val fecha: String,

    val entradaReal: Long?,

    val salidaReal: Long?,

    val entradaLegal: Long?,

    val salidaLegal: Long?,

    val tiempoLegal: Long,

    val tiempoExtraDetectado: Long,

    val cerradaAutomaticamente: Boolean
)