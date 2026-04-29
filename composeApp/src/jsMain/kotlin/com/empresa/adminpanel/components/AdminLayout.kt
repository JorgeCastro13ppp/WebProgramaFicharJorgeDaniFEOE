package com.empresa.adminpanel.components

import androidx.compose.runtime.*
import com.empresa.adminpanel.screens.*
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.*
import style.AppStyles

@Composable
fun AdminLayout(
    onLogout: () -> Unit,
    showToast: (String, String) -> Unit
) {

    var screen by remember { mutableStateOf("dashboard") }

    var historialUserId by remember {
        mutableStateOf<Int?>(null)
    }

    var sidebarOpen by remember {
        mutableStateOf(false)
    }

    var windowWidth by remember {
        mutableStateOf(window.innerWidth)
    }


    /*
    Detectar resize
    */

    LaunchedEffect(Unit) {

        window.onresize = {

            windowWidth = window.innerWidth
        }
    }


    val sidebarStyle = when {

        windowWidth >= 1024 ->
            AppStyles.sidebarDesktop

        windowWidth >= 768 && !sidebarOpen ->
            AppStyles.sidebarTablet

        else ->
            AppStyles.sidebarMobile
    }


    @Composable
    fun navItem(
        label: String,
        icon: String,
        route: String
    ) {

        Div({

            classes(AppStyles.sidebarButton)

            if (windowWidth < 1024 && !sidebarOpen) {

                classes(AppStyles.sidebarCollapsed)
            }

            onClick {

                screen = route

                sidebarOpen = false
            }

        }) {

            Img(icon) {

                classes(AppStyles.sidebarIcon)
            }

            if (windowWidth >= 1024 || sidebarOpen) {

                Text(label)
            }
        }
    }


    Div({

        classes(AppStyles.layout)

    }) {

        /*
        OVERLAY MOBILE
        */

        if (windowWidth < 1024 && sidebarOpen) {

            Div({

                classes(AppStyles.sidebarOverlay)

                onClick {

                    sidebarOpen = false
                }

            })
        }


        /*
        SIDEBAR
        */

        Div({

            classes(sidebarStyle)

            if (windowWidth < 1024 && sidebarOpen) {

                classes(AppStyles.sidebarMobileOpen)
            }

        }) {

            navItem("Dashboard", "/icons/dashboard.svg", "dashboard")

            navItem("Usuarios", "/icons/usuarios.svg", "usuarios")

            navItem("Fichajes", "/icons/fichajes.svg", "fichajes")

            navItem("Vacaciones", "/icons/vacaciones.svg", "vacaciones")

            navItem("Faltas", "/icons/faltas.svg", "faltas")

            navItem("Documentos", "/icons/documentos.svg", "documentos")

            navItem("Horas extra", "/icons/horasextra.svg", "horasextra")

            navItem("Jornadas", "/icons/jornada.svg", "jornadas")
        }


        /*
        CONTENT
        */

        Div({

            classes(AppStyles.content)

        }) {

            TopBar(

                onLogout = onLogout,

                showToast = showToast,

                onHamburgerClick = {

                    sidebarOpen = !sidebarOpen
                }
            )


            when {

                historialUserId != null -> {

                    JornadasUsuarioScreen(

                        userId = historialUserId!!,

                        onBack = {

                            historialUserId = null
                        }
                    )
                }

                screen == "dashboard" -> DashboardScreen()

                screen == "usuarios" -> UsuariosScreen()

                screen == "fichajes" -> FichajesScreen()

                screen == "vacaciones" -> VacacionesScreen()

                screen == "faltas" -> FaltasScreen()

                screen == "documentos" -> DocumentosScreen()

                screen == "horasextra" -> HorasExtrasScreen()

                screen == "jornadas" ->

                    JornadasScreen {

                        historialUserId = it
                    }
            }
        }
    }
}

@Composable
fun sidebarItem(
    id: String,
    current: String,
    onClickAction: () -> Unit,
    icon: String,
    label: String
) {

    Button({

        classes(AppStyles.sidebarButton)

        if (current == id)
            classes(AppStyles.sidebarButtonActive)

        onClick { onClickAction() }

    }) {

        Img(icon) {

            classes(AppStyles.sidebarIcon)
        }

        Span({

            classes(AppStyles.sidebarLabel)

        }) {

            Text(label)
        }
    }
}