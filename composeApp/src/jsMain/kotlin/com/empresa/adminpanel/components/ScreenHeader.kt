package com.empresa.adminpanel.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import style.AppStyles

@Composable
fun ScreenHeader(

    title: String,

    onRefresh: () -> Unit,

    rightContent: @Composable () -> Unit = {}

) {

    Div({

        style {

            display(DisplayStyle.Flex)

            justifyContent(JustifyContent.SpaceBetween)

            alignItems(AlignItems.Center)

            marginBottom(28.px)
        }

    }) {

        /* IZQUIERDA: título + botón recargar */

        Div({

            style {

                display(DisplayStyle.Flex)

                alignItems(AlignItems.Center)

                gap(16.px)
            }

        }) {

            H2({

                classes(AppStyles.title)

            }) {

                Text(title)
            }


            Button({

                classes(AppStyles.secondaryButton)

                onClick {

                    onRefresh()
                }

            }) {

                Img(
                    src = "/icons/refresh.svg",
                    attrs = {
                        classes(AppStyles.buttonIcon)
                    }
                )

                Text("Recargar")
            }
        }


        /* DERECHA: filtros + botones extra */

        Div({

            style {

                display(DisplayStyle.Flex)

                gap(12.px)

                alignItems(AlignItems.Center)
            }

        }) {

            rightContent()
        }
    }
}