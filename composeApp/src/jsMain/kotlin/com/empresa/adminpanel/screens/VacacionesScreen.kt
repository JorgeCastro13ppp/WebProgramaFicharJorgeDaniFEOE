package com.empresa.adminpanel.screens

import androidx.compose.runtime.*
import com.empresa.adminpanel.models.Vacacion
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.dom.*
import org.w3c.fetch.Headers

@Composable
fun VacacionesScreen() {

    var vacaciones by remember {
        mutableStateOf<List<Vacacion>>(emptyList())
    }

    val scope = rememberCoroutineScope()

    fun cargarVacaciones() {

        scope.launch {

            val token = window.localStorage.getItem("token")
                ?: return@launch

            val headers = Headers()

            headers.append("Authorization", "Bearer $token")

            val requestInit = js("{}")
            requestInit.method = "GET"
            requestInit.headers = headers

            val response = window.fetch(
                "http://127.0.0.1:8080/vacaciones",
                requestInit
            ).await()

            if (response.ok) {

                val text = response.text().await()

                vacaciones =
                    Json.decodeFromString(text)
            }
        }
    }

    fun actualizarEstado(id: Int, estado: String) {

        scope.launch {

            val token = window.localStorage.getItem("token")
                ?: return@launch

            val headers = Headers()

            headers.append("Authorization", "Bearer $token")

            val requestInit = js("{}")
            requestInit.method = "PUT"
            requestInit.headers = headers

            window.fetch(
                "http://127.0.0.1:8080/vacaciones/$id?estado=$estado",
                requestInit
            ).await()

            cargarVacaciones()
        }
    }

    LaunchedEffect(Unit) {
        cargarVacaciones()
    }

    Div {

        H2 { Text("Gestión de vacaciones") }

        vacaciones.forEach { vacacion ->

            Div {

                Text(
                    "ID ${vacacion.id} | Usuario ${vacacion.userId} | ${vacacion.fechaInicio} → ${vacacion.fechaFin} | ${vacacion.estado}"
                )

                Button(attrs = {
                    onClick {
                        actualizarEstado(vacacion.id, "aprobado")
                    }
                }) { Text("Aprobar") }

                Button(attrs = {
                    onClick {
                        actualizarEstado(vacacion.id, "rechazado")
                    }
                }) { Text("Rechazar") }

                Br()
            }
        }
    }
}