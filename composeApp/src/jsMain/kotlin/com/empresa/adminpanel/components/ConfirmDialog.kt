package com.empresa.adminpanel.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.*
import style.AppStyles

@Composable
fun ConfirmDialog(
    message: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {

    Div({
        classes(AppStyles.dialogOverlay)
    }) {

        Div({
            classes(AppStyles.dialogBox)
        }) {

            P {
                Text(message)
            }

            Div({
                classes(AppStyles.dialogButtons)
            }) {

                Button({
                    classes(AppStyles.dialogCancel)
                    onClick { onCancel() }
                }) {
                    Text("Cancelar")
                }

                Button({
                    classes(AppStyles.dialogConfirm)
                    onClick { onConfirm() }
                }) {
                    Text("Eliminar")
                }
            }
        }
    }
}