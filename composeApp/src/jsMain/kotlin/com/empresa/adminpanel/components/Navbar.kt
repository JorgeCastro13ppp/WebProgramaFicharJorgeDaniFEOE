package com.empresa.adminpanel.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.css.*
@Composable
fun Navbar(onNavigate: (String) -> Unit) {

    Div({

        style {
            padding(16.px)
        }

    }) {

        Button(attrs = {
            onClick { onNavigate("usuarios") }
        }) { Text("Usuarios") }

        Text(" | ")

        Button(attrs = {
            onClick { onNavigate("fichajes") }
        }) { Text("Fichajes") }

        Text(" | ")

        Button(attrs = {
            onClick { onNavigate("vacaciones") }
        }) { Text("Vacaciones") }

        Text(" | ")

        Button(attrs = {
            onClick { onNavigate("faltas") }
        }) { Text("Faltas") }

        Text(" | ")

        Button(attrs = {
            onClick { onNavigate("documentos") }
        }) { Text("Documentos") }
    }
}