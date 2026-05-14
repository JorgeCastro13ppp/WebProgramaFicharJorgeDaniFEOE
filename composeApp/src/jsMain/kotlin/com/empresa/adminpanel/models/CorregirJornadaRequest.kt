package com.empresa.adminpanel.models

import kotlinx.serialization.Serializable

@Serializable
data class CorregirJornadaRequest(

    val jornadaId: Int,

    val nuevaEntradaReal: Long,

    val nuevaSalidaReal: Long,

    val comentario: String? = null
)