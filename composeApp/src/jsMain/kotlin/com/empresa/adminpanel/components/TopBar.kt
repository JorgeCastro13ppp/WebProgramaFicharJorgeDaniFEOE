package com.empresa.adminpanel.components

import androidx.compose.runtime.*
import com.empresa.adminpanel.screens.obtenerFechaHoraActual
import kotlinx.browser.window
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import style.AppStyles
import kotlin.js.Date

@Composable
fun TopBar(
    onLogout: () -> Unit,
    showToast: (String, String) -> Unit
) {

    val username =
        window.localStorage.getItem("username") ?: "admin"

    var currentTime by remember { mutableStateOf("") }

    // reloj en tiempo real con formato español profesional
    LaunchedEffect(Unit) {

        while (true) {

            val now = Date()

            val formattedDate =
                now.toLocaleDateString(
                    "es-ES",
                    js(
                        """{
                            weekday: "long",
                            year: "numeric",
                            month: "long",
                            day: "numeric"
                        }"""
                    )
                )

            val formattedTime =
                now.toLocaleTimeString("es-ES")

            currentTime =
                "$formattedDate | $formattedTime"

            delay(1000)
        }
    }

    Div({ classes(AppStyles.topbar) }) {

        // título panel
        H3 {

            Text("Panel de Administración")
        }


        // bloque derecha (fecha + usuario + logout)
        Div({

            style {

                display(DisplayStyle.Flex)

                alignItems(AlignItems.Center)

                property("gap", "20px")
            }

        }) {

            // fecha actual dinámica
            Span({

                style {

                    fontSize(14.px)

                    color(Color("#9EA7AD"))
                }

            }) {

                Text(currentTime)
            }


            // usuario logueado
            Span({ classes(AppStyles.username) }) {

                Img(
                    src = "/icons/admin.svg",
                    attrs = {
                        classes(AppStyles.topbarIcon)
                    }
                )

                Text(username)
            }


            // botón logout
            Button(attrs = {

                classes(AppStyles.logoutButton)

                onClick {

                    window.localStorage.removeItem("token")
                    window.localStorage.removeItem("username")

                    showToast(
                        "Sesión cerrada · ${obtenerFechaHoraActual()}",
                        "error"
                    )

                    onLogout()
                }

            }) {

                Img(
                    src = "/icons/logout.svg",
                    attrs = {
                        classes(AppStyles.topbarIcon)
                    }
                )

                Text("Logout")
            }
        }
    }
}