package com.empresa.adminpanel.screens

import androidx.compose.runtime.*
import com.empresa.adminpanel.ApiClient
import com.empresa.adminpanel.components.ScreenHeader
import com.empresa.adminpanel.models.JornadaUsuario
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.dom.*
import org.w3c.fetch.Headers
import style.AppStyles
import kotlin.js.Date


@Composable
fun JornadasUsuarioScreen(
    userId: Int,
    onBack: () -> Unit
) {

    var jornadas by remember {
        mutableStateOf<List<JornadaUsuario>>(emptyList())
    }

    var loading by remember {
        mutableStateOf(true)
    }

    val scope = rememberCoroutineScope()


    /*
    ========================
    CARGAR HISTORIAL
    ========================
    */

    fun cargarHistorial() {

        scope.launch {

            loading = true

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

            val response =
                window.fetch(
                    "${ApiClient.BASE_URL}/admin/jornadas/usuario/$userId",
                    requestInit
                ).await()

            if (response.ok) {

                val text =
                    response.text().await()

                jornadas =
                    Json.decodeFromString(text)
            }

            loading = false
        }
    }


    /*
    ========================
    INIT
    ========================
    */

    LaunchedEffect(userId) {

        cargarHistorial()
    }


    /*
    ========================
    HEADER
    ========================
    */

    ScreenHeader(

        title = "Historial jornadas del trabajador",

        onRefresh = { cargarHistorial() }

    ) {

        Button({

            classes(AppStyles.secondaryButton)

            onClick { onBack() }

        }) {

            Text("← Volver")
        }
    }


    /*
    ========================
    LOADING
    ========================
    */

    if (loading) {

        Div({

            classes(AppStyles.loaderContainer)

        }) {

            Div({

                classes(AppStyles.loader)

            }) {}
        }

        return
    }


    /*
    ========================
    EMPTY STATE
    ========================
    */

    if (jornadas.isEmpty()) {

        P { Text("No hay jornadas registradas para este trabajador") }

        return
    }


    /*
    ========================
    TABLE
    ========================
    */

    Div({

        classes(AppStyles.tableContainer)

    }) {

        Table({

            classes(AppStyles.tableFlat)

        }) {

            Tr {

                Th { Text("Fecha") }
                Th { Text("Entrada") }
                Th { Text("Salida") }
                Th { Text("Tiempo legal") }
                Th { Text("Horas extra") }
                Th { Text("Estado") }
            }


            jornadas.forEach { jornada ->

                Tr {

                    Td {

                        Text(jornada.fecha)
                    }

                    Td {

                        Text(
                            jornada.entradaReal
                                ?.let { Date(it).toLocaleString() }
                                ?: "-"
                        )
                    }

                    Td {

                        Text(
                            jornada.salidaReal
                                ?.let { Date(it).toLocaleString() }
                                ?: "-"
                        )
                    }

                    Td {

                        val minutos =
                            jornada.tiempoLegal / 60000

                        val horas =
                            minutos / 60

                        val resto =
                            minutos % 60

                        Text("${horas}h ${resto}min")
                    }

                    Td {

                        val minutosExtra =
                            jornada.tiempoExtraDetectado / 60000

                        if (minutosExtra > 0) {

                            Text("${minutosExtra} min")

                        } else {

                            Text("-")
                        }
                    }

                    Td {

                        Span({

                            classes(

                                if (jornada.cerradaAutomaticamente)
                                    AppStyles.badgeWarning
                                else
                                    AppStyles.badgeSuccess
                            )

                        }) {

                            Text(

                                if (jornada.cerradaAutomaticamente)
                                    "Automática"
                                else
                                    "Normal"
                            )
                        }
                    }
                }
            }
        }
    }
}