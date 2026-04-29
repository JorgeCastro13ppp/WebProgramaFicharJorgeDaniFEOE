package com.empresa.adminpanel.models

import kotlinx.serialization.Serializable

@Serializable
data class HorasExtrasResumen(

    val pendientes: Int,
    val aprobadas: Int,
    val rechazadas: Int,
    val totalMinutos: Int
)