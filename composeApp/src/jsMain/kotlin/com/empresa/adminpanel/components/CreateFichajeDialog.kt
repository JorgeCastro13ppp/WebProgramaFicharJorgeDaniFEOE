package com.empresa.adminpanel.components

import androidx.compose.runtime.*
import com.empresa.adminpanel.ApiClient
import com.empresa.adminpanel.models.Usuario
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.w3c.fetch.Headers
import style.AppStyles
import kotlin.js.Date

@Composable
fun CreateFichajeDialog(

    usuarios: List<Usuario>,

    onClose: () -> Unit,

    onCreated: (String, String) -> Unit

) {

    var selectedUserId by remember { mutableStateOf("") }

    var fecha by remember { mutableStateOf("") }

    var hora by remember { mutableStateOf("") }

    var contexto by remember { mutableStateOf("TALLER") }

    var accion by remember { mutableStateOf("ENTRADA") }

    var loading by remember { mutableStateOf(false) }


    /* VALIDACIÓN VISUAL */

    var userError by remember { mutableStateOf(false) }

    var fechaError by remember { mutableStateOf(false) }

    var horaError by remember { mutableStateOf(false) }


    val scope = rememberCoroutineScope()


    fun crearFichaje() {

        userError = selectedUserId.isEmpty()

        fechaError = fecha.isEmpty()

        horaError = hora.isEmpty()

        if (userError || fechaError || horaError)
            return


        scope.launch {

            loading = true

            val token =
                window.localStorage.getItem("token")
                    ?: return@launch


            val headers = Headers()

            headers.append("Authorization", "Bearer $token")

            headers.append("Content-Type", "application/json")


            val timestamp =
                Date("$fecha $hora").getTime()


            val bodyObject = js("{}")

            bodyObject["userId"] =
                selectedUserId.toInt()

            bodyObject["timestamp"] =
                timestamp

            bodyObject["contexto"] =
                contexto

            bodyObject["accion"] =
                accion

            bodyObject["latitud"] = 0.0

            bodyObject["longitud"] = 0.0

            bodyObject["accuracy"] = 0.0


            val requestInit = js("{}")

            requestInit.method = "POST"

            requestInit.headers = headers

            requestInit.body =
                JSON.stringify(bodyObject)


            val response = window.fetch(

                "${ApiClient.BASE_URL}/fichajes-eventos",

                requestInit

            ).await()


            loading = false


            if (response.ok) {

                onCreated(accion, contexto)

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

                Text("Crear fichaje")
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


                /* FECHA */

                Input(InputType.Date, attrs = {

                    classes(AppStyles.dialogInput)

                    if (fechaError)
                        classes(AppStyles.dialogInputError)

                    value(fecha)

                    onInput {

                        fecha = it.value

                        fechaError = false
                    }
                })


                if (fechaError) {

                    Span({

                        classes(AppStyles.inputErrorText)

                    }) {

                        Text("Selecciona una fecha")
                    }
                }


                /* HORA */

                Input(InputType.Time, attrs = {

                    classes(AppStyles.dialogInput)

                    if (horaError)
                        classes(AppStyles.dialogInputError)

                    value(hora)

                    onInput {

                        hora = it.value

                        horaError = false
                    }
                })


                if (horaError) {

                    Span({

                        classes(AppStyles.inputErrorText)

                    }) {

                        Text("Selecciona una hora")
                    }
                }


                /* CONTEXTO */

                Select({

                    classes(AppStyles.dialogInput)

                    onChange {

                        contexto = it.target.value
                    }

                }) {

                    Option("TALLER") { Text("Taller") }

                    Option("OBRA") { Text("Obra") }

                    Option("REPARACION") { Text("Reparación") }
                }


                /* ACCIÓN */

                Select({

                    classes(AppStyles.dialogInput)

                    onChange {

                        accion = it.target.value
                    }

                }) {

                    Option("ENTRADA") { Text("Entrada") }

                    Option("SALIDA") { Text("Salida") }

                    Option("INICIO_VIAJE") { Text("Inicio viaje") }

                    Option("FIN_VIAJE") { Text("Fin viaje") }

                    Option("INICIO_DESCANSO") { Text("Inicio descanso") }

                    Option("FIN_DESCANSO") { Text("Fin descanso") }
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
                        || fecha.isEmpty()
                        || hora.isEmpty()
                        || loading
                    ) {

                        disabled()

                        classes(AppStyles.primaryButtonDisabled)
                    }

                    onClick {

                        crearFichaje()
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