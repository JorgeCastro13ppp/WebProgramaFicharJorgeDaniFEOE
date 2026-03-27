package com.empresa.adminpanel.components

import androidx.compose.runtime.*
import com.empresa.adminpanel.models.Usuario
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
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

                        if (selectedUserId.isNotEmpty()) {

                            onConfirm(

                                selectedUserId.toInt(),

                                fecha,

                                tipo,

                                descripcion
                            )
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