package com.empresa.adminpanel.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.gap
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.css.marginTop
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*
import style.AppStyles
@Composable
fun ConfirmDialog(

    message: String,

    confirmText: String,

    confirmClass: String,

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

                style {

                    display(DisplayStyle.Flex)

                    justifyContent(JustifyContent.Center)

                    gap(12.px)

                    marginTop(18.px)
                }

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