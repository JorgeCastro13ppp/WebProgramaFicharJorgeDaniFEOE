package com.empresa.adminpanel.screens

import androidx.compose.runtime.*
import com.empresa.adminpanel.models.Falta
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.dom.*
import org.w3c.fetch.Headers

@Composable
fun FaltasScreen() {

    var faltas by remember {
        mutableStateOf<List<Falta>>(emptyList())
    }

    val scope = rememberCoroutineScope()

    fun cargarFaltas() {

        scope.launch {

            val token = window.localStorage.getItem("token")
                ?: return@launch

            val headers = Headers()

            headers.append("Authorization", "Bearer $token")

            val requestInit = js("{}")
            requestInit.method = "GET"
            requestInit.headers = headers

            val response = window.fetch(
                "http://127.0.0.1:8080/faltas",
                requestInit
            ).await()

            if (response.ok) {

                val text = response.text().await()

                faltas =
                    Json.decodeFromString(text)
            }
        }
    }

    fun eliminarFalta(id: Int) {

        scope.launch {

            val token = window.localStorage.getItem("token")
                ?: return@launch

            val headers = Headers()

            headers.append("Authorization", "Bearer $token")

            val requestInit = js("{}")
            requestInit.method = "DELETE"
            requestInit.headers = headers

            window.fetch(
                "http://127.0.0.1:8080/faltas/$id",
                requestInit
            ).await()

            cargarFaltas()
        }
    }

    LaunchedEffect(Unit) {
        cargarFaltas()
    }

    Div {

        H2 { Text("Gestión de faltas") }

        faltas.forEach { falta ->

            Div {

                Text(
                    "ID ${falta.id} | Usuario ${falta.userId} | ${falta.fecha} | ${falta.tipo}"
                )

                Button(attrs = {
                    onClick {
                        eliminarFalta(falta.id)
                    }
                }) {
                    Text("Eliminar")
                }

                Br()
            }
        }
    }
}