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

        classes(AppStyles.screenHeaderContainer)

    }) {

        /*
        ========================
        BLOQUE IZQUIERDO
        ========================
        */

        Div({

            classes(AppStyles.screenHeaderLeft)

        }) {

            H2({

                classes(AppStyles.title)

            }) {

                Text(title)
            }


            Button({

                classes(AppStyles.secondaryButton)

                attr("type", "button")   // 🔥 SOLUCIÓN

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


        /*
        ========================
        BLOQUE DERECHO
        ========================
        */

        Div({

            classes(AppStyles.screenHeaderRight)

        }) {

            rightContent()
        }
    }
}