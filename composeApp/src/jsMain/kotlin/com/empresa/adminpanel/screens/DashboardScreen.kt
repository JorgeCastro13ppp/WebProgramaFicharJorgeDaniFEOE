package com.empresa.adminpanel.screens

import androidx.compose.runtime.*
import com.empresa.adminpanel.ApiClient
import com.empresa.adminpanel.components.ScreenHeader
import com.empresa.adminpanel.components.StatAccent
import com.empresa.adminpanel.components.StatCard
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.dom.*
import org.w3c.fetch.Headers
import style.AppStyles


@Composable
fun DashboardScreen() {

    var usuarios by remember { mutableStateOf<Int?>(null) }
    var fichajesHoy by remember { mutableStateOf<Int?>(null) }
    var vacacionesPendientes by remember { mutableStateOf<Int?>(null) }
    var faltasActivas by remember { mutableStateOf<Int?>(null) }

    var entradasHoy by remember { mutableStateOf<Int?>(null) }
    var salidasHoy by remember { mutableStateOf<Int?>(null) }
    var viajesHoy by remember { mutableStateOf<Int?>(null) }
    var descansosHoy by remember { mutableStateOf<Int?>(null) }

    var loading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()


    fun cargarDashboard() {

        scope.launch {

            loading = true

            usuarios = null
            fichajesHoy = null
            entradasHoy = null
            salidasHoy = null
            viajesHoy = null
            descansosHoy = null
            vacacionesPendientes = null
            faltasActivas = null

            try {

                val token =
                    window.localStorage.getItem("token")
                        ?: return@launch

                val headers = Headers()

                headers.append(
                    "Authorization",
                    "Bearer $token"
                )

                val requestInit = js("{}")

                requestInit["method"] = "GET"
                requestInit["headers"] = headers


                /* USUARIOS */

                val usuariosResponse =
                    window.fetch(
                        "${ApiClient.BASE_URL}/admin/usuarios",
                        requestInit
                    ).await()

                if (usuariosResponse.ok) {

                    val json =
                        JSON.parse<Array<dynamic>>(
                            usuariosResponse.text().await()
                        )

                    usuarios = json.length
                }


                /* FICHAJES HOY */

                val fichajesResponse =
                    window.fetch(
                        "${ApiClient.BASE_URL}/admin/fichajes-hoy",
                        requestInit
                    ).await()

                if (fichajesResponse.ok) {

                    val json =
                        JSON.parse<dynamic>(
                            fichajesResponse.text().await()
                        )

                    fichajesHoy = json.total as Int
                }


                /* RESUMEN FICHAJES */

                val resumenResponse =
                    window.fetch(
                        "${ApiClient.BASE_URL}/admin/dashboard-fichajes-hoy",
                        requestInit
                    ).await()

                if (resumenResponse.ok) {

                    val json =
                        JSON.parse<dynamic>(
                            resumenResponse.text().await()
                        )

                    entradasHoy = json.entradas as Int
                    salidasHoy = json.salidas as Int
                    viajesHoy = json.viajes as Int
                    descansosHoy = json.descansos as Int
                }


                /* VACACIONES */

                val vacacionesResponse =
                    window.fetch(
                        "${ApiClient.BASE_URL}/vacaciones",
                        requestInit
                    ).await()

                if (vacacionesResponse.ok) {

                    val json =
                        JSON.parse<Array<dynamic>>(
                            vacacionesResponse.text().await()
                        )

                    vacacionesPendientes =
                        json.count {
                            it.estado == "pendiente"
                        }
                }


                /* FALTAS */

                val faltasResponse =
                    window.fetch(
                        "${ApiClient.BASE_URL}/faltas",
                        requestInit
                    ).await()

                if (faltasResponse.ok) {

                    val json =
                        JSON.parse<Array<dynamic>>(
                            faltasResponse.text().await()
                        )

                    faltasActivas = json.length
                }

            } finally {

                loading = false
            }
        }
    }


    LaunchedEffect(Unit) {

        cargarDashboard()
    }


    /* UI */

    Div({

        classes(AppStyles.screenContainer)

    }) {

        ScreenHeader(

            title = "Dashboard",

            onRefresh = {

                cargarDashboard()
            }
        )


        Div({

            classes(AppStyles.dashboardGrid)

        }) {

            StatCard(
                "Usuarios registrados",
                usuarios?.toString(),
                StatAccent.BLUE
            )

            StatCard(
                "Fichajes hoy",
                fichajesHoy?.toString(),
                StatAccent.GREEN
            )

            StatCard(
                "Entradas hoy",
                entradasHoy?.toString(),
                StatAccent.GREEN
            )

            StatCard(
                "Salidas hoy",
                salidasHoy?.toString(),
                StatAccent.RED
            )

            StatCard(
                "Viajes hoy",
                viajesHoy?.toString(),
                StatAccent.BLUE
            )

            StatCard(
                "Descansos hoy",
                descansosHoy?.toString(),
                StatAccent.ORANGE
            )

            StatCard(
                "Vacaciones pendientes",
                vacacionesPendientes?.toString(),
                StatAccent.ORANGE
            )

            StatCard(
                "Faltas activas",
                faltasActivas?.toString(),
                StatAccent.RED
            )
        }
    }
}