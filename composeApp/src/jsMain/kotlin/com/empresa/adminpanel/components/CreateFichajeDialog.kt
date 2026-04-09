package com.empresa.adminpanel.components

import androidx.compose.runtime.*
import com.empresa.adminpanel.models.Usuario
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
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

    val scope = rememberCoroutineScope()


    fun crearFichaje() {

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

                "http://127.0.0.1:8080/fichajes-eventos",

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

            H3 {

                Text("Crear fichaje")
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


            /* FECHA */

            Input(InputType.Date, attrs = {

                classes(AppStyles.loginInput)

                value(fecha)

                onInput {

                    fecha = it.value
                }
            })


            /* HORA */

            Input(InputType.Time, attrs = {

                classes(AppStyles.loginInput)

                value(hora)

                onInput {

                    hora = it.value
                }
            })


            /* CONTEXTO */

            Select({

                classes(AppStyles.loginInput)

                onChange {

                    contexto = it.target.value
                }

            }) {

                Option("TALLER") {

                    Text("Taller")
                }

                Option("OBRA") {

                    Text("Obra")
                }

                Option("REPARACION") {

                    Text("Reparación")
                }
            }


            /* ACCION */

            Select({

                classes(AppStyles.loginInput)

                onChange {

                    accion = it.target.value
                }

            }) {

                Option("ENTRADA") {

                    Text("Entrada")
                }

                Option("SALIDA") {

                    Text("Salida")
                }

                Option("INICIO_VIAJE") {

                    Text("Inicio viaje")
                }

                Option("FIN_VIAJE") {

                    Text("Fin viaje")
                }

                Option("INICIO_DESCANSO") {

                    Text("Inicio descanso")
                }

                Option("FIN_DESCANSO") {

                    Text("Fin descanso")
                }
            }


            /* BOTONES */

            Div({

                style {

                    display(DisplayStyle.Flex)

                    gap(10.px)

                    marginTop(16.px)
                }

            }) {

                Button({

                    classes(AppStyles.primaryButton)

                    onClick {

                        if (

                            selectedUserId.isNotEmpty()

                            && fecha.isNotEmpty()

                            && hora.isNotEmpty()

                        ) {

                            crearFichaje()
                        }
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