package com.empresa.adminpanel.components

import androidx.compose.runtime.*
import com.empresa.adminpanel.ApiClient
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.disabled
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


    /* VALIDACIÓN VISUAL */

    var usernameError by remember { mutableStateOf(false) }

    var passwordError by remember { mutableStateOf(false) }


    val scope = rememberCoroutineScope()


    fun crearUsuario() {

        usernameError = username.isBlank()

        passwordError = password.isBlank()

        if (usernameError || passwordError)
            return


        scope.launch {

            loading = true

            val token =
                window.localStorage.getItem("token")
                    ?: return@launch


            val headers = Headers()

            headers.append("Authorization", "Bearer $token")

            headers.append("Content-Type", "application/json")


            val bodyObject = js("{}")

            bodyObject["username"] = username

            bodyObject["password"] = password


            val body = JSON.stringify(bodyObject)


            val requestInit = js("{}")

            requestInit.method = "POST"

            requestInit.headers = headers

            requestInit.body = body


            val response = window.fetch(
                "${ApiClient.BASE_URL}/register",
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

            H3({

                classes(AppStyles.dialogTitle)

            }) {

                Text("Crear usuario")
            }


            Div({

                classes(AppStyles.dialogForm)

            }) {

                /* USERNAME */

                Input(InputType.Text, attrs = {

                    classes(AppStyles.dialogInput)

                    if (usernameError)
                        classes(AppStyles.dialogInputError)

                    placeholder("Usuario")

                    value(username)

                    onInput {

                        username = it.value

                        usernameError = false
                    }

                })


                if (usernameError) {

                    Span({

                        classes(AppStyles.inputErrorText)

                    }) {

                        Text("Introduce un nombre de usuario")
                    }
                }


                /* PASSWORD */

                Input(InputType.Password, attrs = {

                    classes(AppStyles.dialogInput)

                    if (passwordError)
                        classes(AppStyles.dialogInputError)

                    placeholder("Contraseña")

                    value(password)

                    onInput {

                        password = it.value

                        passwordError = false
                    }

                })


                if (passwordError) {

                    Span({

                        classes(AppStyles.inputErrorText)

                    }) {

                        Text("Introduce una contraseña")
                    }
                }
            }


            Div({

                classes(AppStyles.dialogActions)

            }) {

                Button(attrs = {

                    classes(AppStyles.secondaryButton)

                    onClick {

                        onClose()
                    }

                }) {

                    Text("Cancelar")
                }


                Button(attrs = {

                    classes(AppStyles.primaryButton)

                    if (
                        username.isBlank()
                        || password.isBlank()
                        || loading
                    ) {

                        disabled()

                        classes(AppStyles.primaryButtonDisabled)
                    }

                    onClick {

                        crearUsuario()
                    }

                }) {

                    if (loading) {

                        Div({

                            classes(AppStyles.loaderSmall)

                        }) {}

                    } else {

                        Text("Crear")
                    }
                }
            }
        }
    }
}