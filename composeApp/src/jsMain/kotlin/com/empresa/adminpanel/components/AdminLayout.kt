package com.empresa.adminpanel.components

import androidx.compose.runtime.*
import com.empresa.adminpanel.screens.*
import org.jetbrains.compose.web.dom.*
import style.AppStyles

@Composable
fun AdminLayout(
    onLogout: () -> Unit,
    showToast: (String, String) -> Unit
) {

    var screen by remember { mutableStateOf("dashboard") }

    Div({ classes(AppStyles.layout) }) {

        // Sidebar
        Div({ classes(AppStyles.sidebar) }) {

            H3({
                classes(AppStyles.sidebarTitle)
            }) {

                Text("Admin Panel")
            }

            Button(attrs = {

                classes(AppStyles.sidebarButton)

                if (screen == "dashboard") {
                    classes(AppStyles.sidebarButtonActive)
                }

                onClick { screen = "dashboard" }

            }) {
                Img(
                    src = "/icons/dashboard.svg",
                    attrs = {
                        classes(AppStyles.sidebarIcon)
                    }
                )
                Text("Dashboard")
            }

            Button(attrs = {

                classes(AppStyles.sidebarButton)

                if (screen == "usuarios") {
                    classes(AppStyles.sidebarButtonActive)
                }

                onClick { screen = "usuarios" }

            }) {
                Img(
                    src = "/icons/usuarios.svg",
                    attrs = {
                        classes(AppStyles.sidebarIcon)
                    }
                )
                Text("Usuarios")
            }

            Button(attrs = {

                classes(AppStyles.sidebarButton)

                if (screen == "fichajes") {
                    classes(AppStyles.sidebarButtonActive)
                }

                onClick { screen = "fichajes" }

            }) {
                Img(
                    src = "/icons/fichajes.svg",
                    attrs = {
                        classes(AppStyles.sidebarIcon)
                    }
                )
                Text("Fichajes") }

            Button(attrs = {

                classes(AppStyles.sidebarButton)

                if (screen == "vacaciones") {
                    classes(AppStyles.sidebarButtonActive)
                }

                onClick { screen = "vacaciones" }

            }) {
                Img(
                    src = "/icons/vacaciones.svg",
                    attrs = {
                        classes(AppStyles.sidebarIcon)
                    }
                )
                Text("Vacaciones") }

            Button(attrs = {

                classes(AppStyles.sidebarButton)

                if (screen == "faltas") {
                    classes(AppStyles.sidebarButtonActive)
                }

                onClick { screen = "faltas" }

            }) {
                Img(
                    src = "/icons/faltas.svg",
                    attrs = {
                        classes(AppStyles.sidebarIcon)
                    }
                )
                Text("Faltas") }

            Button(attrs = {

                classes(AppStyles.sidebarButton)

                if (screen == "documentos") {
                    classes(AppStyles.sidebarButtonActive)
                }

                onClick { screen = "documentos" }

            }) {
                Img(
                    src = "/icons/documentos.svg",
                    attrs = {
                        classes(AppStyles.sidebarIcon)
                    }
                )
                Text("Documentos") }
        }

        // Content area
        Div({ classes(AppStyles.content, AppStyles.screenFade) }) {

            TopBar(
                onLogout = onLogout,
                showToast = showToast
            )

            when (screen) {

                "dashboard" -> DashboardScreen()

                "usuarios" -> UsuariosScreen()

                "fichajes" -> FichajesScreen()

                "vacaciones" -> VacacionesScreen()

                "faltas" -> FaltasScreen()

                "documentos" -> DocumentosScreen()
            }
        }
    }
}

