package com.empresa.adminpanel

import androidx.compose.runtime.*
import com.empresa.adminpanel.components.Navbar
import com.empresa.adminpanel.screens.*
import org.jetbrains.compose.web.renderComposable

fun main() {

    renderComposable(rootElementId = "root") {

        App()
    }
}

@Composable
fun App() {

    var loggedIn by remember { mutableStateOf(false) }
    var screen by remember { mutableStateOf("usuarios") }

    if (!loggedIn) {

        LoginScreen {
            loggedIn = true
        }

    } else {

        Navbar { selected ->
            screen = selected
        }

        when (screen) {

            "usuarios" -> UsuariosScreen()

            "fichajes" -> FichajesScreen()

            "vacaciones" -> VacacionesScreen()

            "faltas" -> FaltasScreen()

            "documentos" -> DocumentosScreen()
        }
    }
}