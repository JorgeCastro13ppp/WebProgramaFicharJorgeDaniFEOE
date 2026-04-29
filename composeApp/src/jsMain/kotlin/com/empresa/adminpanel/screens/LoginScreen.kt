package com.empresa.adminpanel.screens

import androidx.compose.runtime.*
import com.empresa.adminpanel.ApiClient
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.attributes.*
import org.w3c.fetch.Headers
import style.AppStyles

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

fun obtenerFechaHoraActual(): String {

    val now = kotlin.js.Date()

    val dia = now.getDate().toString().padStart(2, '0')
    val mes = (now.getMonth() + 1).toString().padStart(2, '0')
    val año = now.getFullYear()

    val horas = now.getHours().toString().padStart(2, '0')
    val minutos = now.getMinutes().toString().padStart(2, '0')
    val segundos = now.getSeconds().toString().padStart(2, '0')

    return "$dia/$mes/$año $horas:$minutos:$segundos"
}

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    showToast: (String, String) -> Unit
) {

    var username by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }

    var loading by remember { mutableStateOf(false) }

    var error by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()


    fun login() {

        if (username.isBlank() || password.isBlank())
            return

        scope.launch {

            loading = true

            val headers = Headers()

            headers.append("Content-Type", "application/json")

            val body = Json.encodeToString(
                LoginRequest(username, password)
            )

            val requestInit = js("{}")

            requestInit["method"] = "POST"
            requestInit["headers"] = headers
            requestInit["body"] = body

            val response = window.fetch(
                "${ApiClient.BASE_URL}/login",
                requestInit
            ).await()

            loading = false

            if (response.ok) {

                val json =
                    JSON.parse<dynamic>(
                        response.text().await()
                    )

                window.localStorage.setItem(
                    "token",
                    json.token as String
                )

                ApiClient.setToken(
                    json.token as String
                )

                window.localStorage.setItem(
                    "username",
                    username
                )

                showToast(
                    "Sesión iniciada · ${obtenerFechaHoraActual()}",
                    "success"
                )

                onLoginSuccess()

            } else {

                error = "Usuario o contraseña incorrectos"
            }
        }
    }


    Div({

        classes(AppStyles.loginContainer)

    }) {

        Div({

            classes(AppStyles.loginCard)

        }) {

            /*
            ========================
            LOGO
            ========================
            */

            Img(

                src = "icons/logo.png",

                attrs = {

                    classes(AppStyles.loginLogo)
                }
            )


            /*
            ========================
            TITLE
            ========================
            */

            H2({

                classes(AppStyles.loginTitle)

            }) {

                Text("Panel de Administración")
            }


            /*
            ========================
            USERNAME
            ========================
            */

            Input(InputType.Text, attrs = {

                classes(AppStyles.loginInput)

                placeholder("Usuario")

                value(username)

                onInput {

                    username = it.value

                    error = null
                }

                onKeyDown {

                    if (it.key == "Enter")
                        login()
                }
            })


            /*
            ========================
            PASSWORD
            ========================
            */

            Input(InputType.Password, attrs = {

                classes(AppStyles.loginInput)

                placeholder("Contraseña")

                value(password)

                onInput {

                    password = it.value

                    error = null
                }

                onKeyDown {

                    if (it.key == "Enter")
                        login()
                }
            })


            /*
            ========================
            ERROR MESSAGE
            ========================
            */

            if (error != null) {

                P({

                    classes(AppStyles.loginError)

                }) {

                    Text(error!!)
                }
            }


            /*
            ========================
            BUTTON
            ========================
            */

            Button({

                classes(AppStyles.loginButton)

                if (username.isBlank() || password.isBlank() || loading)
                    disabled()

                onClick {

                    login()
                }

            }) {

                if (loading) {

                    Div({

                        classes(AppStyles.loaderSmall)

                    }) {}

                } else {

                    Text("Acceder")
                }
            }
        }
    }
}