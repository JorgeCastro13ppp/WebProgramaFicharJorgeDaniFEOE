package com.empresa.adminpanel.screens

import androidx.compose.runtime.*
import com.empresa.adminpanel.ApiClient
import com.empresa.adminpanel.components.ScreenHeader
import com.empresa.adminpanel.models.JornadaUsuario
import com.empresa.adminpanel.models.Usuario
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.attributes.value
import org.jetbrains.compose.web.dom.*
import org.w3c.fetch.Headers
import style.AppStyles
import kotlin.js.Date
@Composable
fun JornadasGlobalScreen() {

    var jornadas by remember { mutableStateOf<List<JornadaUsuario>>(emptyList()) }
    var usuarios by remember { mutableStateOf<List<Usuario>>(emptyList()) }
    //var selectedUserId by remember { mutableStateOf("todos") }
    var selectedUserId by remember { mutableStateOf<Int?>(null) }
    var loading by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()

    /*
    ========================
    CARGAR USUARIOS
    ========================
    */
    fun cargarUsuarios() {

        scope.launch {

            val token = window.localStorage.getItem("token") ?: return@launch

            val headers = Headers()
            headers.append("Authorization", "Bearer $token")

            val requestInit = js("{}")
            requestInit["method"] = "GET"
            requestInit["headers"] = headers

            val response = window.fetch(
                "${ApiClient.BASE_URL}/admin/usuarios",
                requestInit
            ).await()

            if (response.ok) {
                val text = response.text().await()
                val json = Json {
                    ignoreUnknownKeys = true
                }

                usuarios = json.decodeFromString(text)
            }
        }
    }

    /*
    ========================
    CARGAR JORNADAS
    ========================
    */
    fun cargarJornadas() {

        scope.launch {

            loading = true

            val token = window.localStorage.getItem("token") ?: return@launch

            val headers = Headers()
            headers.append("Authorization", "Bearer $token")

            val requestInit = js("{}")
            requestInit["method"] = "GET"
            requestInit["headers"] = headers

            val url =
                if (selectedUserId == null)
                    "${ApiClient.BASE_URL}/admin/jornadas"
                else
                    "${ApiClient.BASE_URL}/admin/jornadas?userId=$selectedUserId"

            val response = window.fetch(url, requestInit).await()
            val json = Json {
                ignoreUnknownKeys = true
            }
            if (response.ok) {
                val text = response.text().await()
                jornadas = json.decodeFromString(text)
            }

            loading = false
        }
    }

    fun formatHora(timestamp: Long): String {

        val options = js("""
        ({
          hour: '2-digit',
          minute: '2-digit'
        })
    """)

        return Date(timestamp).toLocaleTimeString("es-ES", options)
    }

    fun formatFecha(timestamp: Long): String {
        return Date(timestamp).toLocaleDateString("es-ES")
    }

    /*
    ========================
    INIT
    ========================
    */
    LaunchedEffect(selectedUserId) {
        cargarUsuarios()
        cargarJornadas()
    }

    /*
    ========================
    HEADER
    ========================
    */
    ScreenHeader(
        title = "Jornadas globales",
        onRefresh = { cargarJornadas() }
    ) {

        Select({

            classes(AppStyles.filterSelect)

            onChange {

                val value = it.target.asDynamic().value as String

                println("VALOR SELECT: $value")   // 👈 TEST

                selectedUserId =
                    if (value == "todos") null
                    else value.toInt()

                println("USER ID FINAL: $selectedUserId") // 👈 TEST
            }

        }) {

            Option("todos") {
                Text("Todos los usuarios")
            }

            usuarios.forEach {
                Option(it.id.toString()) {
                    Text(it.username)
                }
            }
        }
    }

    /*
    ========================
    LOADING
    ========================
    */
    if (loading) {
        P { Text("Cargando...") }
        return
    }

    if (jornadas.isEmpty()) {
        P { Text("No hay jornadas registradas") }
        return
    }

    /*
    ========================
    TABLA
    ========================
    */
    Div({
        classes(AppStyles.tableContainer)
    }) {

        Table({
            classes(AppStyles.tableFlat)
        }) {

            Tr {
                Th { Text("Usuario") }
                Th { Text("Fecha") }
                Th { Text("Entrada") }
                Th { Text("Salida") }
                Th { Text("Tiempo legal") }
                Th { Text("Extra") }
                Th { Text("Tiempo real") }
                Th { Text("Estado") }
            }

            jornadas.forEach { j ->

                Tr {

                    Td({
                        classes(AppStyles.urlCell)
                    }) { Text(j.username ?: "-") }

                    Td { Text(j.fecha) }

                    Td {
                        Text(
                            j.entradaReal
                                ?.let { formatHora(it) }
                                ?: "-"
                        )
                    }

                    Td {
                        Text(
                            j.salidaReal
                                ?.let { formatHora(it) }
                                ?: "-"
                        )
                    }

                    Td {

                        val minutos = j.tiempoLegal / 60000
                        val horas = minutos / 60
                        val resto = minutos % 60

                        Text("${horas}h ${resto}min")
                    }



                    Td {

                        val extra = j.tiempoExtraDetectado / 60000

                        Text(if (extra > 0) "$extra min" else "-")
                    }

                    /*
                    ========================
                    TIEMPO REAL
                    ========================
                    */

                    Td {

                        val minutos = j.tiempoTrabajoReal / 60000
                        val horas = minutos / 60
                        val resto = minutos % 60

                        Text("${horas}h ${resto}min")
                    }

                    Td({

                        classes(AppStyles.statusCell)

                    }) {

                        val esCritica =
                            j.cerradaAutomaticamente &&
                                    j.tiempoExtraDetectado > 0

                        Span({

                            classes(

                                when {

                                    j.tipoIncidencia == "CORREGIDA" ->
                                        AppStyles.badgeInfo

                                    esCritica ->
                                        AppStyles.badgeDanger

                                    j.cerradaAutomaticamente ->
                                        AppStyles.badgeWarning

                                    j.tipoIncidencia == "EXTRA" ->
                                        AppStyles.badgePrimary

                                    else ->
                                        AppStyles.badgeSuccess
                                }
                            )

                        }) {

                            Text(

                                when {

                                    j.tipoIncidencia == "CORREGIDA" ->
                                        "Corregida"

                                    esCritica ->
                                        "Auto + Extra"

                                    j.cerradaAutomaticamente ->
                                        "Automática"

                                    j.tipoIncidencia == "EXTRA" ->
                                        "Extra"

                                    else ->
                                        "Normal"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}