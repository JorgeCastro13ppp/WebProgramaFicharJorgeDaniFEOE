package com.empresa.adminpanel.models

import kotlinx.serialization.Serializable

@Serializable
data class JornadaUsuario(

    val id: Int,

    val userId: Int,
    val username: String,

    val fecha: String,

    val entradaReal: Long?,

    val salidaReal: Long?,

    val entradaLegal: Long? = null,

    val salidaLegal: Long?= null,

    val tiempoLegal: Long,

    val tiempoTrabajoReal: Long,

    val tiempoExtraDetectado: Long,

    val cerradaAutomaticamente: Boolean
)