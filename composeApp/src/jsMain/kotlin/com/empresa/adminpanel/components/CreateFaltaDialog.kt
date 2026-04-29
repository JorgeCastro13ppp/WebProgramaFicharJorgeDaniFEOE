package com.empresa.adminpanel.components

import androidx.compose.runtime.*
import com.empresa.adminpanel.ApiClient
import com.empresa.adminpanel.models.Usuario
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.w3c.fetch.Headers
import style.AppStyles

@Composable
fun CreateFaltaDialog(

    usuarios: List<Usuario>,

    onConfirm: (
        Int,
        String,
        String,
        String
    ) -> Unit,

    onError: (String) -> Unit,

    onCancel: () -> Unit

) {

    var selectedUserId by remember { mutableStateOf("") }

    var fecha by remember { mutableStateOf("") }

    var tipo by remember { mutableStateOf("retraso") }

    var descripcion by remember { mutableStateOf("") }


    /* VALIDACIÓN VISUAL */

    var userError by remember { mutableStateOf(false) }

    var fechaError by remember { mutableStateOf(false) }


    val scope = rememberCoroutineScope()


    Div({

        classes(AppStyles.dialogOverlay)

    }) {

        Div({

            classes(AppStyles.dialogBox)

        }) {

            H3({

                classes(AppStyles.dialogTitle)

            }) {

                Text("Registrar falta")
            }


            Div({

                classes(AppStyles.dialogForm)

            }) {

                /* USUARIO */

                Select({

                    classes(AppStyles.dialogInput)

                    if (userError) {
                        classes(AppStyles.dialogInputError)
                    }

                    onChange {

                        selectedUserId = it.target.value

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

                    if (fechaError) {
                        classes(AppStyles.dialogInputError)
                    }

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


                /* TIPO */

                Select({

                    classes(AppStyles.dialogInput)

                    onChange {

                        tipo = it.target.value
                    }

                }) {

                    Option("retraso") { Text("Retraso") }

                    Option("justificada") { Text("Justificada") }

                    Option("injustificada") { Text("Injustificada") }
                }


                /* DESCRIPCIÓN */

                Input(InputType.Text, attrs = {

                    classes(AppStyles.dialogInput)

                    placeholder("Descripción")

                    value(descripcion)

                    onInput {

                        descripcion = it.value
                    }
                })
            }


            Div({

                classes(AppStyles.dialogActions)

            }) {

                Button(attrs = {

                    classes(AppStyles.secondaryButton)

                    onClick {

                        onCancel()
                    }

                }) {

                    Text("Cancelar")
                }


                Button(attrs = {

                    classes(AppStyles.primaryButton)

                    if (
                        selectedUserId.isEmpty()
                        || fecha.isEmpty()
                    ) {
                        disabled()
                        classes(AppStyles.primaryButtonDisabled)
                    }

                    onClick {

                        userError = selectedUserId.isEmpty()

                        fechaError = fecha.isEmpty()

                        if (userError || fechaError)
                            return@onClick


                        scope.launch {

                            val token =
                                window.localStorage.getItem("token")
                                    ?: return@launch


                            val headers = Headers()

                            headers.append("Authorization", "Bearer $token")
                            headers.append("Content-Type", "application/json")


                            val bodyObject = js("{}")

                            bodyObject["fecha"] = fecha
                            bodyObject["tipo"] = tipo
                            bodyObject["descripcion"] = descripcion


                            val requestInit = js("{}")

                            requestInit.method = "POST"
                            requestInit.headers = headers
                            requestInit.body = JSON.stringify(bodyObject)


                            val response = window.fetch(
                                "${ApiClient.BASE_URL}/faltas/$selectedUserId",
                                requestInit
                            ).await()


                            if (response.ok) {

                                onConfirm(
                                    selectedUserId.toInt(),
                                    fecha,
                                    tipo,
                                    descripcion
                                )

                            } else {

                                val json =
                                    JSON.parse<dynamic>(
                                        response.text().await()
                                    )

                                onError(json.message as String)
                            }
                        }
                    }

                }) {

                    Text("Guardar")
                }
            }
        }
    }
}