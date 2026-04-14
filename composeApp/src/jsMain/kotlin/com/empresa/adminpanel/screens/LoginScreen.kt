package com.empresa.adminpanel.screens

import androidx.compose.runtime.*
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.css.CSSNumeric
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.css.marginBottom
import org.jetbrains.compose.web.css.marginLeft
import org.jetbrains.compose.web.css.marginRight
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
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
){

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    fun login() {

        scope.launch {

            val headers = Headers()
            headers.append("Content-Type", "application/json")

            val body = Json.encodeToString(
                LoginRequest(username, password)
            )

            val requestInit = js("{}")

            requestInit.method = "POST"
            requestInit.headers = headers
            requestInit.body = body

            val response = window.fetch(
                "http://127.0.0.1:8080/login",
                requestInit
            ).await()

            if (response.ok) {

                val json =
                    JSON.parse<dynamic>(
                        response.text().await()
                    )

                window.localStorage.setItem(
                    "token",
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
            }
        }
    }

    Div({
        classes(AppStyles.loginContainer)
    }) {

        Div({
            classes(AppStyles.loginCard)
        }) {

            Img(
                src = "/icons/admin.svg",
                attrs = {
                    style {
                        width(48.px)
                        marginBottom(14.px)
                        display(DisplayStyle.Block)
                        marginLeft(auto as CSSNumeric)
                        marginRight(auto as CSSNumeric)
                    }
                }
            )

            H2({
                classes(AppStyles.loginTitle)
            }) {
                Text("Admin Panel")
            }

            Input(InputType.Text, attrs = {

                classes(AppStyles.loginInput)

                attr("placeholder", "Usuario")

                value(username)

                onInput { event ->
                    username = event.value
                }
            })

            Input(InputType.Password, attrs = {

                classes(AppStyles.loginInput)

                attr("placeholder", "Contraseña")

                value(password)

                onInput { event ->
                    password = event.value
                }
            })

            Button({

                classes(AppStyles.loginButton)

                onClick {
                    login()
                }

            }) {
                Text("Acceder")
            }
        }
    }
}