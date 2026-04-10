package com.empresa.adminpanel.components

import androidx.compose.runtime.*
import com.empresa.adminpanel.models.Usuario
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.*
import org.w3c.fetch.Headers
import style.AppStyles

@Composable
fun CreateVacacionDialog(

    usuarios: List<Usuario>,
    onError: (String) -> Unit,
    onClose: () -> Unit,

    onCreated: (String) -> Unit

) {

    var selectedUserId by remember { mutableStateOf("") }

    var fechaInicio by remember { mutableStateOf("") }

    var fechaFin by remember { mutableStateOf("") }

    var loading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()


    fun crearVacacion() {

        if (
            selectedUserId.isEmpty()
            || fechaInicio.isEmpty()
            || fechaFin.isEmpty()
        ) return


        scope.launch {

            loading = true

            val token =
                window.localStorage.getItem("token")
                    ?: return@launch


            val headers = Headers()

            headers.append(
                "Authorization",
                "Bearer $token"
            )

            headers.append(
                "Content-Type",
                "application/json"
            )


            val bodyObject = js("{}")

            bodyObject["fechaInicio"] = fechaInicio
            bodyObject["fechaFin"] = fechaFin


            val requestInit = js("{}")

            requestInit.method = "POST"

            requestInit.headers = headers

            requestInit.body =
                JSON.stringify(bodyObject)


            val response = window.fetch(

                "http://127.0.0.1:8080/admin/vacaciones/$selectedUserId",

                requestInit

            ).await()


            loading = false


            if (response.ok) {

                val usuarioSeleccionado =
                    usuarios.find {
                        it.id == selectedUserId.toInt()
                    }

                onCreated(usuarioSeleccionado?.username ?: "usuario")

                onClose()

            } else {

            val json =
                JSON.parse<dynamic>(
                    response.text().await()
                )

            onError(json.message as String)
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

                Text("Crear vacaciones")
            }


            /* USUARIO */

            Select({

                classes(AppStyles.loginInput)

                onChange {

                    selectedUserId =
                        it.target.value
                }

            }) {

                Option("") {

                    Text("Seleccionar usuario")
                }

                usuarios.forEach {

                    Option(it.id.toString()) {

                        Text(it.username)
                    }
                }
            }


            /* FECHA INICIO */

            Input(InputType.Date, attrs = {

                classes(AppStyles.loginInput)

                value(fechaInicio)

                onInput {

                    fechaInicio = it.value
                }
            })


            /* FECHA FIN */

            Input(InputType.Date, attrs = {

                classes(AppStyles.loginInput)

                value(fechaFin)

                onInput {

                    fechaFin = it.value
                }
            })


            /* BOTONES */

            Div({

                classes(AppStyles.dialogButtons)

            }) {

                Button({

                    classes(AppStyles.primaryButton)

                    onClick {

                        crearVacacion()
                    }

                }) {

                    if (loading) {

                        Div({

                            classes(AppStyles.loader)

                        }) {}

                    } else {

                        Text("Guardar")
                    }
                }


                Button({

                    classes(AppStyles.secondaryButton)

                    onClick {

                        onClose()
                    }

                }) {

                    Text("Cancelar")
                }
            }
        }
    }
}