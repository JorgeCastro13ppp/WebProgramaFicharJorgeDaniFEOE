package com.empresa.adminpanel

object ApiClient {

    const val BASE_URL = "http://127.0.0.1:8080"

    var token: String? = null

    fun authHeader(): String =
        "Bearer $token"
}