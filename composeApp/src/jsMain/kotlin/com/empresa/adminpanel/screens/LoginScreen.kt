package com.empresa.adminpanel.screens

import androidx.compose.runtime.*
import com.empresa.adminpanel.ApiClient
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.attributes.InputType
import org.w3c.fetch.Headers
import kotlin.js.JSON
import kotlin.js.json

private val scope = MainScope()

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Div {

        H2 { Text("Admin Panel Login") }

        Input(type = InputType.Text, attrs = {
            onInput { username = it.value }
        })

        Br()

        Input(type = InputType.Password, attrs = {
            onInput { password = it.value }
        })

        Br()

        Button(attrs = {

            onClick {

                scope.launch {

                    try {

                        val requestInit = js("{}")

                        requestInit.method = "POST"

                        requestInit.headers = Headers().apply {
                            append("Content-Type", "application/json")
                        }

                        requestInit.body = JSON.stringify(
                            json(
                                "username" to username,
                                "password" to password
                            )
                        )

                        val response = window.fetch(
                            "${ApiClient.BASE_URL}/login",
                            requestInit
                        ).await()

                        if (response.ok) {

                            val text = response.text().await()

                            val json = JSON.parse<dynamic>(text)

                            val token = json.token as String

                            window.localStorage.setItem("token", token)

                            onLoginSuccess()
                        } else {

                            errorMessage = "Credenciales incorrectas"
                        }

                    } catch (e: Exception) {

                        errorMessage = "Error conectando con el servidor"
                    }
                }
            }
        }) {

            Text("Login")
        }

        if (errorMessage.isNotEmpty()) {

            P {
                Text(errorMessage)
            }
        }
    }
}