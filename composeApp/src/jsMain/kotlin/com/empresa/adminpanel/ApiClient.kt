package com.empresa.adminpanel

import kotlinx.browser.window

object ApiClient {

    const val BASE_URL = "https://192.168.1.32:8443"

    var token: String? =
        window.localStorage.getItem("token")

    fun setToken(value: String) {

        token = value

        window.localStorage.setItem(
            "token",
            value
        )
    }

    fun authHeader(): String =
        "Bearer $token"
}