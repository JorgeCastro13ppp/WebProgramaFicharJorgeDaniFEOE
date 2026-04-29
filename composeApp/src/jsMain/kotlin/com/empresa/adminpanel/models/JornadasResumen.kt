package com.empresa.adminpanel.models

import kotlinx.serialization.Serializable

@Serializable
data class JornadasResumen(

    val totalJornadas: Int,

    val jornadasAutomaticas: Int,

    val jornadasCorregidas: Int
)