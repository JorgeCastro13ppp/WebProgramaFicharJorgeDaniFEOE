package com.empresa.adminpanel.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.*
import style.AppStyles
@Composable
fun ConfirmDialog(

    message: String,
    confirmText: String,
    confirmClass: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    extraContent: (@Composable (() -> Unit))? = null

) {

    Div({

        classes(AppStyles.dialogOverlay)

    }) {

        Div({

            classes(AppStyles.dialogBox)

        }) {

            /*
            ========================
            MENSAJE
            ========================
            */

            P({

                classes(AppStyles.dialogMessage)

            }) {

                Text(message)
            }


            /*
            ========================
            EXTRA CONTENT
            ========================
            */

            extraContent?.invoke()


            /*
            ========================
            BOTONES
            ========================
            */

            Div({

                classes(AppStyles.dialogActions)

            }) {

                Button({

                    classes(AppStyles.secondaryButton)

                    onClick {

                        onCancel()
                    }

                }) {

                    Text("Cancelar")
                }


                Button({

                    classes(confirmClass)

                    onClick {

                        onConfirm()
                    }

                }) {

                    Text(confirmText)
                }
            }
        }
    }
}