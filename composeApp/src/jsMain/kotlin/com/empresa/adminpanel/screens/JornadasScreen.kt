package com.empresa.adminpanel.screens

import androidx.compose.runtime.*
import com.empresa.adminpanel.ApiClient
import com.empresa.adminpanel.api.actualizarJornada
import com.empresa.adminpanel.components.ConfirmDialog
import com.empresa.adminpanel.components.ScreenHeader
import com.empresa.adminpanel.components.StatAccent
import com.empresa.adminpanel.components.StatCard
import com.empresa.adminpanel.components.Toast
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

    var fechaEntrada by remember { mutableStateOf("") }
    var horaEntrada by remember { mutableStateOf("") }

    var fechaSalida by remember { mutableStateOf("") }
    var horaSalida by remember { mutableStateOf("") }

    var toastMessage by remember {

        mutableStateOf<String?>(null)
    }

    var toastType by remember {

        mutableStateOf("success")
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
                    "${ApiClient.BASE_URL}/jornadas/pendientes-revision"
                else
                    "${ApiClient.BASE_URL}/jornadas/pendientes-revision?userId=$selectedUserId"

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

            val url =
                if (selectedUserId == "todos") {
                    "${ApiClient.BASE_URL}/jornadas/resumen-pendientes"
                } else {
                    "${ApiClient.BASE_URL}/jornadas/resumen-pendientes?userId=$selectedUserId"
                }

            val response =
                window.fetch(url, requestInit).await()

            // 🔥 CONTROL DE ERROR (A TU ESTILO)
            if (!response.ok) {

                console.error("Error cargando resumen:", response.status)

                resumen = null

                return@launch
            }

            // ✅ SOLO SI TODO OK
            val text =
                response.text().await()

            val json = Json {
                ignoreUnknownKeys = true
            }

            resumen = json.decodeFromString(text)
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
    CORREGIR JORNADA
    ========================
    */

    fun guardarCorreccionJornada() {

        if (
            fechaEntrada.isBlank() ||
            horaEntrada.isBlank() ||
            fechaSalida.isBlank() ||
            horaSalida.isBlank()
        ) {

            toastMessage =
                "Completa todos los campos"

            toastType =
                "warning"

            return
        }


        val nuevaEntradaTimestamp =

            Date(
                "${fechaEntrada}T${horaEntrada}"
            ).getTime().toLong()


        val nuevaSalidaTimestamp =

            Date(
                "${fechaSalida}T${horaSalida}"
            ).getTime().toLong()


        if (
            nuevaSalidaTimestamp <=
            nuevaEntradaTimestamp
        ) {

            toastMessage =
                "La salida debe ser posterior a la entrada"

            toastType =
                "warning"

            return
        }


        scope.launch {

            try {

                actualizarJornada(

                    jornadaId =
                        selectedJornadaId!!,

                    nuevaEntradaReal =
                        nuevaEntradaTimestamp,

                    nuevaSalidaReal =
                        nuevaSalidaTimestamp
                )


                /*
                ========================
                RESET
                ========================
                */

                selectedJornadaId = null

                fechaEntrada = ""
                horaEntrada = ""

                fechaSalida = ""
                horaSalida = ""


                /*
                ========================
                RECARGAR
                ========================
                */

                cargarPendientes()
                cargarResumen()


                /*
                ========================
                TOAST
                ========================
                */

                toastMessage =
                    "Jornada corregida correctamente"

                toastType =
                    "success"

            } catch (e: Exception) {

                toastMessage =
                    e.message ?: "Error"

                toastType =
                    "error"
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
                                ?.let { formatHora(it) }
                                ?: "-"
                        )
                    }

                    Td {

                        Text(
                            jornada.salidaReal
                                ?.let { formatHora(it) }
                                ?: "-"
                        )
                    }


                    Td({

                        classes(AppStyles.actionCell)

                    }) {

                        Button({

                            classes(AppStyles.primaryButton)

                            onClick {

                                selectedJornadaId =
                                    jornada.id


                                jornada.entradaReal?.let {

                                    val entrada =
                                        Date(it)

                                    fechaEntrada =
                                        entrada.toISOString()
                                            .substring(0, 10)

                                    horaEntrada =
                                        entrada.toTimeString()
                                            .substring(0, 5)
                                }


                                jornada.salidaReal?.let {

                                    val salida =
                                        Date(it)

                                    fechaSalida =
                                        salida.toISOString()
                                            .substring(0, 10)

                                    horaSalida =
                                        salida.toTimeString()
                                            .substring(0, 5)
                                }
                            }

                        }) {

                            Img(
                                src = "/icons/revisar.svg",
                                attrs = {
                                    classes(AppStyles.deleteIcon)
                                }
                            )

                            Text("Revisar")
                        }
                    }


                    Td({

                        classes(AppStyles.actionCell)

                    }) {

                        Button({

                            classes(AppStyles.secondaryButton)

                            onClick {

                                onOpenHistorial(jornada.userId)
                            }

                        }) {

                            Img(
                                src = "/icons/historial.svg",
                                attrs = {
                                    classes(AppStyles.deleteIcon)
                                }
                            )

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

                Div({

                    classes(AppStyles.modifyDiv)

                }) {

                    /*
                    ========================
                    ENTRADA
                    ========================
                    */

                    Label {

                        Text("Fecha entrada")
                    }

                    Input(InputType.Date) {

                        classes(AppStyles.input)

                        value(fechaEntrada)

                        onInput {

                            fechaEntrada =
                                it.value
                        }
                    }


                    Label {

                        Text("Hora entrada")
                    }

                    Input(InputType.Time) {

                        classes(AppStyles.input)

                        value(horaEntrada)

                        onInput {

                            horaEntrada =
                                it.value
                        }
                    }


                    /*
                    ========================
                    SALIDA
                    ========================
                    */

                    Label {

                        Text("Fecha salida")
                    }

                    Input(InputType.Date) {

                        classes(AppStyles.input)

                        value(fechaSalida)

                        onInput {

                            fechaSalida =
                                it.value
                        }
                    }


                    Label {

                        Text("Hora salida")
                    }

                    Input(InputType.Time) {

                        classes(AppStyles.input)

                        value(horaSalida)

                        onInput {

                            horaSalida =
                                it.value
                        }
                    }
                }
            },

            onConfirm = {

                guardarCorreccionJornada()
            },

            onCancel = {

                selectedJornadaId = null

                fechaEntrada = ""
                horaEntrada = ""

                fechaSalida = ""
                horaSalida = ""
            }
        )
    }

    toastMessage?.let {

        Toast(

            message = it,

            type = toastType,

            onClose = {

                toastMessage = null
            }
        )
    }
}