package com.empresa.adminpanel.components

import androidx.compose.runtime.*
import com.empresa.adminpanel.screens.obtenerFechaHoraActual
import kotlinx.browser.window
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.gap
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.*
import style.AppStyles
import kotlin.js.Date

@Composable
fun TopBar(
    onLogout: () -> Unit,
    showToast: (String, String) -> Unit,
    onHamburgerClick: () -> Unit
) {

    val username =
        window.localStorage.getItem("username") ?: "admin"

    var currentTime by remember { mutableStateOf("") }


    /*
    ========================
    RELOJ EN TIEMPO REAL
    ========================
    */

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


    /*
    ========================
    TOPBAR UI
    ========================
    */

    Div({

        classes(AppStyles.topbar)

    }) {

        /*
        ========================
        IZQUIERDA
        ========================
        */

        Div({

            style {

                display(DisplayStyle.Flex)

                alignItems(AlignItems.Center)

                gap(12.px)
            }

        }) {

            /*
            HAMBURGER (solo móvil)
            */

            Button({

                classes(AppStyles.hamburgerButton)

                onClick { onHamburgerClick() }

            }) {

                Img("/icons/menu.svg") {

                    style {

                        width(30.px)

                        height(30.px)
                    }
                }
            }


            H3({

                classes(AppStyles.topbarTitle)

            }) {

                Text("Panel de Administración")
            }
        }


        /*
        ========================
        DERECHA
        ========================
        */

        Div({

            classes(AppStyles.topbarRight)

        }) {

            Span({

                classes(AppStyles.topbarClock)

            }) {

                Text(currentTime)
            }


            Span({

                classes(AppStyles.username)

            }) {

                Img(
                    src = "/icons/admin.svg",
                    attrs = {
                        classes(AppStyles.topbarIcon)
                    }
                )

                Text(username)
            }


            Button({

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

                Text("Salir")
            }
        }
    }
}