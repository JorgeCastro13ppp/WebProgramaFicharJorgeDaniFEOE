package com.empresa.adminpanel.screens

import androidx.compose.runtime.*
import com.empresa.adminpanel.models.Fichaje
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.dom.*
import org.w3c.fetch.Headers
import kotlin.js.Date

@Composable
fun FichajesScreen() {

    var fichajes by remember {
        mutableStateOf<List<Fichaje>>(emptyList())
    }

    val scope = rememberCoroutineScope()

    fun cargarFichajes() {

        scope.launch {

            val token = window.localStorage.getItem("token")
                ?: return@launch

            val headers = Headers()

            headers.append("Authorization", "Bearer $token")

            val requestInit = js("{}")
            requestInit.method = "GET"
            requestInit.headers = headers

            val response = window.fetch(
                "http://127.0.0.1:8080/admin/fichajes",
                requestInit
            ).await()

            if (response.ok) {

                val text = response.text().await()

                fichajes =
                    Json.decodeFromString<List<Fichaje>>(text)
            }
        }
    }

    fun eliminarFichaje(id: Int) {

        scope.launch {

            val token = window.localStorage.getItem("token")
                ?: return@launch

            val headers = Headers()

            headers.append("Authorization", "Bearer $token")

            val requestInit = js("{}")
            requestInit.method = "DELETE"
            requestInit.headers = headers

            window.fetch(
                "http://127.0.0.1:8080/admin/fichajes/$id",
                requestInit
            ).await()

            cargarFichajes()
        }
    }

    LaunchedEffect(Unit) {
        cargarFichajes()
    }

    Div {

        H2 { Text("Historial de fichajes") }

        fichajes.forEach { fichaje ->

            Div {

                val fechaFormateada =
                    Date(fichaje.fechaHora).toLocaleString()

                Text(
                    "ID ${fichaje.id} | Usuario ${fichaje.userId} | ${fichaje.tipo} | $fechaFormateada"
                )

                Button(attrs = {
                    onClick {
                        eliminarFichaje(fichaje.id)
                    }
                }) {
                    Text("Eliminar")
                }

                Br()
            }
        }
    }
}