package com.empresa.adminpanel.components

import androidx.compose.runtime.*
import com.empresa.adminpanel.ApiClient
import com.empresa.adminpanel.models.Usuario
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.disabled
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


    /* VALIDACIÓN VISUAL */

    var userError by remember { mutableStateOf(false) }

    var fechaInicioError by remember { mutableStateOf(false) }

    var fechaFinError by remember { mutableStateOf(false) }

    var rangoError by remember { mutableStateOf(false) }


    val scope = rememberCoroutineScope()


    fun validar(): Boolean {

        userError = selectedUserId.isEmpty()

        fechaInicioError = fechaInicio.isEmpty()

        fechaFinError = fechaFin.isEmpty()

        rangoError =
            !fechaInicioError &&
                    !fechaFinError &&
                    fechaInicio > fechaFin


        return !(userError || fechaInicioError || fechaFinError || rangoError)
    }


    fun crearVacacion() {

        if (!validar())
            return


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

                "${ApiClient.BASE_URL}/admin/vacaciones/$selectedUserId",

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

            H3({

                classes(AppStyles.dialogTitle)

            }) {

                Text("Crear vacaciones")
            }


            Div({

                classes(AppStyles.dialogForm)

            }) {

                /* USUARIO */

                Select({

                    classes(AppStyles.dialogInput)

                    if (userError)
                        classes(AppStyles.dialogInputError)

                    onChange {

                        selectedUserId =
                            it.target.value

                        userError = false
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


                if (userError) {

                    Span({

                        classes(AppStyles.inputErrorText)

                    }) {

                        Text("Selecciona un usuario")
                    }
                }


                /* FECHA INICIO */

                Input(InputType.Date, attrs = {

                    classes(AppStyles.dialogInput)

                    if (fechaInicioError || rangoError)
                        classes(AppStyles.dialogInputError)

                    value(fechaInicio)

                    onInput {

                        fechaInicio = it.value

                        fechaInicioError = false
                        rangoError = false
                    }
                })


                if (fechaInicioError) {

                    Span({

                        classes(AppStyles.inputErrorText)

                    }) {

                        Text("Selecciona una fecha de inicio")
                    }
                }


                /* FECHA FIN */

                Input(InputType.Date, attrs = {

                    classes(AppStyles.dialogInput)

                    if (fechaFinError || rangoError)
                        classes(AppStyles.dialogInputError)

                    value(fechaFin)

                    onInput {

                        fechaFin = it.value

                        fechaFinError = false
                        rangoError = false
                    }
                })


                if (fechaFinError) {

                    Span({

                        classes(AppStyles.inputErrorText)

                    }) {

                        Text("Selecciona una fecha de fin")
                    }
                }


                if (rangoError) {

                    Span({

                        classes(AppStyles.inputErrorText)

                    }) {

                        Text("La fecha fin no puede ser anterior a la fecha inicio")
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
                        selectedUserId.isEmpty()
                        || fechaInicio.isEmpty()
                        || fechaFin.isEmpty()
                        || fechaInicio > fechaFin
                        || loading
                    ) {

                        disabled()

                        classes(AppStyles.primaryButtonDisabled)
                    }

                    onClick {

                        crearVacacion()
                    }

                }) {

                    if (loading) {

                        Div({

                            classes(AppStyles.loaderSmall)

                        }) {}

                    } else {

                        Text("Guardar")
                    }
                }
            }
        }
    }
}