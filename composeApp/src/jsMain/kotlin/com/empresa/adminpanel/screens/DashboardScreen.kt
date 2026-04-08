package com.empresa.adminpanel.screens

import androidx.compose.runtime.*
import com.empresa.adminpanel.components.StatAccent
import com.empresa.adminpanel.components.StatCard
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.marginTop
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*
import org.w3c.fetch.Headers
import style.AppStyles
import kotlin.js.Date

@Composable
fun DashboardScreen() {

    var usuarios by remember { mutableStateOf(0) }
    var fichajesHoy by remember { mutableStateOf(0) }
    var vacacionesPendientes by remember { mutableStateOf(0) }
    var faltasActivas by remember { mutableStateOf(0) }

    var entradasHoy by remember { mutableStateOf(0) }
    var salidasHoy by remember { mutableStateOf(0) }
    var viajesHoy by remember { mutableStateOf(0) }
    var descansosHoy by remember { mutableStateOf(0) }

    var loading by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()

    fun cargarDashboard() {

        scope.launch {

            val token = window.localStorage.getItem("token")
                ?: return@launch

            val headers = Headers()

            headers.append("Authorization", "Bearer $token")

            val requestInit = js("{}")

            requestInit.method = "GET"
            requestInit.headers = headers


            // 👤 USUARIOS
            val usuariosResponse =
                window.fetch(
                    "http://127.0.0.1:8080/admin/usuarios",
                    requestInit
                ).await()

            if (usuariosResponse.ok) {

                val text = usuariosResponse.text().await()

                val json = JSON.parse<Array<dynamic>>(text)

                usuarios = json.length
            }


            // ⏱ FICHAJES HOY
            val fichajesResponse =
                window.fetch(
                    "http://127.0.0.1:8080/admin/fichajes-hoy",
                    requestInit
                ).await()

            if (fichajesResponse.ok) {

                val text = fichajesResponse.text().await()

                val json = JSON.parse<dynamic>(text)

                fichajesHoy = json.total as Int
            }

            // 📊 RESUMEN FICHAJES HOY
            val resumenResponse =
                window.fetch(
                    "http://127.0.0.1:8080/admin/dashboard-fichajes-hoy",
                    requestInit
                ).await()

            if (resumenResponse.ok) {

                val text = resumenResponse.text().await()

                val json = JSON.parse<dynamic>(text)

                entradasHoy = json.entradas as Int
                salidasHoy = json.salidas as Int
                viajesHoy = json.viajes as Int
                descansosHoy = json.descansos as Int
            }


            // 🌴 VACACIONES PENDIENTES
            val vacacionesResponse =
                window.fetch(
                    "http://127.0.0.1:8080/vacaciones",
                    requestInit
                ).await()

            if (vacacionesResponse.ok) {

                val text = vacacionesResponse.text().await()

                val json = JSON.parse<Array<dynamic>>(text)

                vacacionesPendientes =
                    json.count {
                        it.estado == "pendiente"
                    }
            }


            // ⚠ FALTAS ACTIVAS
            val faltasResponse =
                window.fetch(
                    "http://127.0.0.1:8080/faltas",
                    requestInit
                ).await()

            if (faltasResponse.ok) {

                val text = faltasResponse.text().await()

                val json = JSON.parse<Array<dynamic>>(text)

                faltasActivas = json.length
            }
        }
    }

    LaunchedEffect(Unit) {
        cargarDashboard()
        loading = false
    }

    if (loading) {

        Div({
            classes(AppStyles.loaderContainer)
        }) {
            Div({
                classes(AppStyles.loader)
            }) {}
        }

    } else {

        Div {

            H2({
                classes(AppStyles.title)
            }) {
                Text("Dashboard")
            }

            Div({
                classes(AppStyles.dashboardGrid)
            }) {

                StatCard(
                    "Usuarios registrados",
                    usuarios.toString(),
                    StatAccent.BLUE
                )

                StatCard(
                    "Fichajes hoy",
                    fichajesHoy.toString(),
                    StatAccent.GREEN
                )

                StatCard(
                    "Entradas hoy",
                    entradasHoy.toString(),
                    StatAccent.GREEN
                )

                StatCard(
                    "Salidas hoy",
                    salidasHoy.toString(),
                    StatAccent.RED
                )

                StatCard(
                    "Viajes hoy",
                    viajesHoy.toString(),
                    StatAccent.BLUE
                )

                StatCard(
                    "Descansos hoy",
                    descansosHoy.toString(),
                    StatAccent.ORANGE
                )

                StatCard(
                    "Vacaciones pendientes",
                    vacacionesPendientes.toString(),
                    StatAccent.ORANGE
                )

                StatCard(
                    "Faltas activas",
                    faltasActivas.toString(),
                    StatAccent.RED
                )
            }
        }
    }
}