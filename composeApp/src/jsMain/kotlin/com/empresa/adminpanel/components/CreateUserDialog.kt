package com.empresa.adminpanel.components

import androidx.compose.runtime.*
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.attributes.value
import org.jetbrains.compose.web.dom.*
import org.w3c.fetch.Headers
import style.AppStyles

@Composable
fun CreateUserDialog(
    onClose: () -> Unit,
    onCreated: (String) -> Unit
) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    fun crearUsuario() {

        scope.launch {

            loading = true

            val token =
                window.localStorage.getItem("token")
                    ?: return@launch

            val headers = Headers()

            headers.append("Authorization", "Bearer $token")
            headers.append("Content-Type", "application/json")

            // ✅ objeto dinámico correcto Kotlin/JS
            val bodyObject = js("{}")
            bodyObject["username"] = username
            bodyObject["password"] = password

            val body = JSON.stringify(bodyObject)

            val requestInit = js("{}")
            requestInit.method = "POST"
            requestInit.headers = headers
            requestInit.body = body

            val response = window.fetch(
                "http://127.0.0.1:8080/register",
                requestInit
            ).await()

            loading = false

            if (response.ok) {

                onCreated(username)
                onClose()
            }
        }
    }

    Div({
        classes(AppStyles.dialogOverlay)
    }) {

        Div({
            classes(AppStyles.dialogBox)
        }) {

            H3 {
                Text("Crear usuario")
            }

            Input(InputType.Text, attrs = {

                classes(AppStyles.loginInput)

                placeholder("Usuario")

                value(username)

                onInput {
                    username = it.value
                }

            })

            Input(InputType.Password, attrs = {

                classes(AppStyles.loginInput)

                placeholder("Contraseña")

                value(password)

                onInput {
                    password = it.value
                }

            })

            Div({
                classes(AppStyles.dialogButtons)
            }) {

                Button(attrs = {

                    classes(AppStyles.dialogCancel)

                    onClick { onClose() }

                }) {
                    Text("Cancelar")
                }

                Button(attrs = {

                    classes(AppStyles.dialogConfirm)

                    onClick { crearUsuario() }

                }) {

                    if (loading) {

                        Div({
                            classes(AppStyles.loader)
                        }) {}

                    } else {

                        Text("Crear")
                    }
                }
            }
        }
    }
}