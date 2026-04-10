package com.empresa.adminpanel.components

import androidx.compose.runtime.*
import com.empresa.adminpanel.models.Usuario
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
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

    var selectedUserId by remember {
        mutableStateOf("")
    }

    var fecha by remember {
        mutableStateOf("")
    }

    var tipo by remember {
        mutableStateOf("retraso")
    }

    var descripcion by remember {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()


    Div({

        classes(AppStyles.dialogOverlay)

    }) {

        Div({

            classes(AppStyles.dialogBox)

        }) {

            H3 {

                Text("Registrar falta")
            }


            /* USUARIO */

            Select({

                classes(AppStyles.loginInput)

                onChange {

                    selectedUserId = it.target.value
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


            /* TIPO */

            Select({

                classes(AppStyles.loginInput)

                onChange {

                    tipo = it.target.value
                }

            }) {

                Option("retraso") {
                    Text("Retraso")
                }

                Option("justificada") {
                    Text("Justificada")
                }

                Option("injustificada") {
                    Text("Injustificada")
                }
            }


            /* DESCRIPCIÓN */

            Input(InputType.Text, attrs = {

                classes(AppStyles.loginInput)

                placeholder("Descripción")

                value(descripcion)

                onInput {

                    descripcion = it.value
                }
            })


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

                        if (selectedUserId.isEmpty()) return@onClick

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

                                "http://127.0.0.1:8080/faltas/$selectedUserId",

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


                Button({

                    classes(AppStyles.secondaryButton)

                    onClick {

                        onCancel()
                    }

                }) {

                    Text("Cancelar")
                }
            }
        }
    }
}