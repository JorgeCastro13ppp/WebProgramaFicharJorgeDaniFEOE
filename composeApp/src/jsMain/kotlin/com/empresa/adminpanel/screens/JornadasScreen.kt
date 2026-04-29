package com.empresa.adminpanel.screens

import androidx.compose.runtime.*
import com.empresa.adminpanel.ApiClient
import com.empresa.adminpanel.components.ConfirmDialog
import com.empresa.adminpanel.components.ScreenHeader
import com.empresa.adminpanel.components.StatAccent
import com.empresa.adminpanel.components.StatCard
import com.empresa.adminpanel.models.JornadaPendiente
import com.empresa.adminpanel.models.JornadasResumen
import com.empresa.adminpanel.models.Usuario
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.*
import org.w3c.fetch.Headers
import style.AppStyles
import kotlin.js.Date


@Composable
fun JornadasScreen(onOpenHistorial: (Int) -> Unit) {

    var jornadas by remember {
        mutableStateOf<List<JornadaPendiente>>(emptyList())
    }

    var resumen by remember {
        mutableStateOf<JornadasResumen?>(null)
    }

    var usuarios by remember {
        mutableStateOf<List<Usuario>>(emptyList())
    }

    var selectedUserId by remember {
        mutableStateOf("todos")
    }

    var selectedUserHistorialId by remember {
        mutableStateOf<Int?>(null)
    }

    var selectedJornadaId by remember {
        mutableStateOf<Int?>(null)
    }

    var nuevaEntrada by remember {
        mutableStateOf<Long?>(null)
    }

    var nuevaSalida by remember {
        mutableStateOf<Long?>(null)
    }

    var loading by remember {
        mutableStateOf(true)
    }

    val scope = rememberCoroutineScope()


    /*
    ========================
    CARGAR USUARIOS
    ========================
    */

    fun cargarUsuarios() {

        scope.launch {

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
                    "${ApiClient.BASE_URL}/admin/usuarios",
                    requestInit
                ).await()

            if (response.ok) {

                val text =
                    response.text().await()

                usuarios =
                    Json.decodeFromString(text)
            }
        }
    }


    /*
    ========================
    CARGAR PENDIENTES
    ========================
    */

    fun cargarPendientes() {

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

            val url =
                if (selectedUserId == "todos")
                    "${ApiClient.BASE_URL}/admin/jornadas/pendientes"
                else
                    "${ApiClient.BASE_URL}/admin/jornadas/pendientes?userId=$selectedUserId"

            val response =
                window.fetch(url, requestInit)
                    .await()

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
    CARGAR RESUMEN
    ========================
    */

    fun cargarResumen() {

        scope.launch {

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
                    "${ApiClient.BASE_URL}/admin/jornadas/resumen",
                    requestInit
                ).await()

            if (response.ok) {

                val text =
                    response.text().await()

                resumen =
                    Json.decodeFromString(text)
            }
        }
    }


    /*
    ========================
    CORREGIR JORNADA
    ========================
    */

    fun corregirJornada() {

        val id =
            selectedJornadaId ?: return

        scope.launch {

            val token =
                window.localStorage.getItem("token")
                    ?: return@launch

            val headers = Headers()

            headers.append(
                "Authorization",
                "Bearer $token"
            )

            headers.append(
                "Content-Type",
                "application/json"
            )

            val body =
                """
                {
                    "entradaReal": $nuevaEntrada,
                    "salidaReal": $nuevaSalida
                }
                """.trimIndent()

            val requestInit = js("{}")

            requestInit["method"] = "PUT"
            requestInit["headers"] = headers
            requestInit["body"] = body

            val response =
                window.fetch(
                    "${ApiClient.BASE_URL}/admin/jornadas/$id/corregir",
                    requestInit
                ).await()

            if (response.ok) {

                selectedJornadaId = null

                nuevaEntrada = null
                nuevaSalida = null

                cargarPendientes()
                cargarResumen()
            }
        }
    }


    /*
    ========================
    INIT
    ========================
    */

    LaunchedEffect(Unit) {

        cargarUsuarios()
        cargarPendientes()
        cargarResumen()
    }


    /*
    ========================
    HEADER
    ========================
    */

    ScreenHeader(

        title = "Jornadas pendientes revisión",

        onRefresh = {

            cargarPendientes()
            cargarResumen()
        }

    ) {

        Select({

            classes(AppStyles.filterSelect)

            onChange {

                selectedUserId =
                    it.target.value

                cargarPendientes()
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
    STATS
    ========================
    */

    Div({

        classes(AppStyles.statsGrid)

    }) {

        StatCard(
            "Total jornadas",
            (resumen?.totalJornadas ?: 0).toString(),
            StatAccent.BLUE
        )

        StatCard(
            "Automáticas",
            (resumen?.jornadasAutomaticas ?: 0).toString(),
            StatAccent.YELLOW
        )

        StatCard(
            "Corregidas",
            (resumen?.jornadasCorregidas ?: 0).toString(),
            StatAccent.GREEN
        )
    }


    /*
    ========================
    TABLA
    ========================
    */

    if (loading) {

        P { Text("Cargando...") }

        return
    }

    if (jornadas.isEmpty()) {

        P { Text("No hay jornadas pendientes") }

        return
    }


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
                Th { Text("Estado") }
                Th { Text("Acción") }
                Th { Text("Historial") }
            }


            jornadas.forEach { jornada ->

                Tr {

                    Td { Text(jornada.username) }

                    Td { Text(jornada.fecha) }

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


                    /*
                    ========================
                    BADGE ESTADO AUTOMÁTICO
                    ========================
                    */

                    Td {

                        val esCritica =
                            jornada.cerradaAutomaticamente &&
                                    jornada.tiempoExtraDetectado > 0

                        Span({

                            classes(

                                when {

                                    esCritica ->
                                        AppStyles.badgeDanger

                                    jornada.cerradaAutomaticamente ->
                                        AppStyles.badgeWarning

                                    else ->
                                        AppStyles.badgeSuccess
                                }
                            )

                        }) {

                            Text(

                                when {

                                    esCritica ->
                                        "Auto + Extra"

                                    jornada.cerradaAutomaticamente ->
                                        "Automática"

                                    else ->
                                        "Normal"
                                }
                            )
                        }
                    }


                    Td {

                        Button({

                            classes(AppStyles.primaryButton)

                            onClick {

                                selectedJornadaId =
                                    jornada.id

                                nuevaEntrada =
                                    jornada.entradaReal

                                nuevaSalida =
                                    jornada.salidaReal
                            }

                        }) {

                            Text("Revisar")
                        }
                    }


                    Td {

                        Button({

                            classes(AppStyles.secondaryButton)

                            onClick {

                                onOpenHistorial(jornada.userId)
                            }

                        }) {

                            Text("Historial")
                        }
                    }
                }
            }
        }
    }


    /*
    ========================
    DIALOG CORRECCIÓN
    ========================
    */

    if (selectedJornadaId != null) {

        ConfirmDialog(

            message = "Corregir jornada",

            confirmText = "Guardar cambios",

            confirmClass =
                AppStyles.primaryButton,

            extraContent = {

                Div {

                    Label {
                        Text("Nueva entrada")
                    }

                    Input(
                        type = InputType.DateTimeLocal
                    ) {

                        value(
                            nuevaEntrada
                                ?.let {
                                    Date(it)
                                        .toISOString()
                                        .substring(0, 16)
                                } ?: ""
                        )

                        onInput {

                            nuevaEntrada =
                                it.value?.let {
                                    Date(it).getTime().toLong()
                                }
                        }
                    }


                    Label {
                        Text("Nueva salida")
                    }

                    Input(
                        type = InputType.DateTimeLocal
                    ) {

                        value(
                            nuevaSalida
                                ?.let {
                                    Date(it)
                                        .toISOString()
                                        .substring(0, 16)
                                } ?: ""
                        )

                        onInput {

                            nuevaSalida =
                                it.value?.let {
                                    Date(it).getTime().toLong()
                                }
                        }
                    }
                }
            },

            onConfirm = {

                corregirJornada()
            },

            onCancel = {

                selectedJornadaId = null

                nuevaEntrada = null
                nuevaSalida = null
            }
        )
    }
}