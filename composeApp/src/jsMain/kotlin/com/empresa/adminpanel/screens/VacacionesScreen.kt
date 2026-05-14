package com.empresa.adminpanel.screens

import androidx.compose.runtime.*
import com.empresa.adminpanel.ApiClient
import com.empresa.adminpanel.components.ConfirmDialog
import com.empresa.adminpanel.components.CreateVacacionDialog
import com.empresa.adminpanel.components.ScreenHeader
import com.empresa.adminpanel.components.StatAccent
import com.empresa.adminpanel.components.StatCard
import com.empresa.adminpanel.components.Toast
import com.empresa.adminpanel.models.Usuario
import com.empresa.adminpanel.models.Vacacion
import com.empresa.adminpanel.models.VacacionesResumenAdminResponse
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.gap
import org.jetbrains.compose.web.css.marginBottom
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*
import org.w3c.fetch.Headers
import style.AppStyles

@Composable
fun VacacionesScreen() {

    var vacaciones by remember {
        mutableStateOf<List<Vacacion>>(emptyList())
    }
    var usuarios by remember {
        mutableStateOf<List<Usuario>>(emptyList())
    }

    var selectedUserId by remember {
        mutableStateOf("todos")
    }

    var selectedVacacionId by remember { mutableStateOf<Int?>(null) }

    var nuevoEstado by remember { mutableStateOf("") }

    var selectedEstado by remember {
        mutableStateOf("todos")
    }

    var showCreateDialog by remember {
        mutableStateOf(false)
    }

    var resumenGlobal by remember {
        mutableStateOf<List<VacacionesResumenAdminResponse>>(emptyList())
    }

    val totalNavidadRestantes =
        resumenGlobal.sumOf {
            it.diasNavidadRestantes
        }

    val totalLibresRestantes =
        resumenGlobal.sumOf {
            it.diasLibresRestantes
        }

    val totalDiasRestantes =
        resumenGlobal.sumOf {
            it.diasTotalesRestantes
        }

    val pendientesAprobacion =
        vacaciones.count {
            it.estado == "pendiente"
        }

    var showDialog by remember { mutableStateOf(false) }
    var loading by remember {
        mutableStateOf(true)
    }

    var toastMessage by remember {
        mutableStateOf<String?>(null)
    }

    var toastType by remember {
        mutableStateOf("success")
    }

    var selectedUsername by remember {
        mutableStateOf<String?>(null)
    }

    val vacacionesFiltradas = vacaciones.filter {

        (selectedEstado == "todos" ||
                it.estado == selectedEstado)

                &&

                (selectedUserId == "todos" ||
                        it.userId.toString() == selectedUserId)
    }

    var sortBy by remember { mutableStateOf("inicio") }
    var order by remember { mutableStateOf("desc") }

    val scope = rememberCoroutineScope()

    fun cargarVacaciones() {

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


            val params = mutableListOf<String>()


            if (selectedUserId != "todos") {

                params.add("userId=$selectedUserId")
            }


            if (selectedEstado != "todos") {

                params.add("estado=$selectedEstado")
            }


            if (sortBy.isNotBlank()) {

                params.add("sortBy=$sortBy")
            }


            if (order.isNotBlank()) {

                params.add("order=$order")
            }


            val queryString =
                if (params.isNotEmpty())
                    "?" + params.joinToString("&")
                else
                    ""


            val url =
                "${ApiClient.BASE_URL}/vacaciones$queryString"


            val requestInit = js("{}")

            requestInit.method = "GET"
            requestInit.headers = headers


            val response =
                window.fetch(
                    url,
                    requestInit
                ).await()


            if (response.ok) {

                val text =
                    response.text().await()

                vacaciones =
                    Json.decodeFromString(text)
            }

            loading = false
        }
    }

    fun cargarUsuarios() {

        scope.launch {

            val token = window.localStorage.getItem("token")
                ?: return@launch

            val headers = Headers()

            headers.append(
                "Authorization",
                "Bearer $token"
            )

            val requestInit = js("{}")

            requestInit.method = "GET"
            requestInit.headers = headers

            val response = window.fetch(
                "${ApiClient.BASE_URL}/admin/usuarios",
                requestInit
            ).await()

            if (response.ok) {

                val text = response.text().await()

                usuarios = Json.decodeFromString(text)
            }
        }
    }
    fun actualizarEstado(
        id: Int,
        estado: String
    ) {

        scope.launch {

            val token = window.localStorage.getItem("token")
                ?: return@launch

            val headers = Headers()

            headers.append("Authorization", "Bearer $token")

            val requestInit = js("{}")

            requestInit.method = "PUT"
            requestInit.headers = headers

            window.fetch(
                "${ApiClient.BASE_URL}/vacaciones/$id?estado=$estado",
                requestInit
            ).await()

            cargarVacaciones()
        }
    }

    fun cargarResumenGlobal() {

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

            requestInit.method = "GET"
            requestInit.headers = headers

            val response =
                window.fetch(
                    "${ApiClient.BASE_URL}/admin/vacaciones/resumen",
                    requestInit
                ).await()

            if (response.ok) {

                val text =
                    response.text().await()

                resumenGlobal =
                    Json.decodeFromString(text)
            }
        }
    }

    if (showCreateDialog) {

        CreateVacacionDialog(

            usuarios = usuarios,

            onClose = {

                showCreateDialog = false
            },

            onError = { mensaje ->

                showCreateDialog = false

                toastMessage = mensaje
                toastType = "warning"
            },

            onCreated = { username ->

                cargarVacaciones()

                toastMessage =
                    "Vacaciones creadas para $username"

                toastType = "success"
            }
        )
    }

    LaunchedEffect(Unit) {

        cargarVacaciones()

        cargarUsuarios()

        cargarResumenGlobal()
    }

    Div {

        ScreenHeader(

            title = "Gestión de vacaciones",

            onRefresh = {

                cargarVacaciones()
            }

        ) {
            Select({

                classes(AppStyles.filterSelect)

                onChange {

                    sortBy = it.target.value

                    cargarVacaciones()
                }

            }) {

                Option("inicio") { Text("Fecha inicio") }

                Option("fin") { Text("Fecha fin") }

                Option("usuario") { Text("Usuario") }

                Option("estado") { Text("Estado") }

                Option("id") { Text("ID") }
            }

            Select({

                classes(AppStyles.filterSelect)

                onChange {

                    order = it.target.value

                    cargarVacaciones()
                }

            }) {

                Option("desc") {

                    Text("Descendente")
                }

                Option("asc") {

                    Text("Ascendente")
                }
            }

            /* FILTRO USUARIO */

            Select({

                classes(AppStyles.filterSelect)

                onChange {

                    selectedUserId = it.target.value
                }

            }) {

                Option("todos") {

                    Text("Todos los usuarios")
                }

                usuarios.forEach { usuario ->

                    Option(usuario.id.toString()) {

                        Text(usuario.username)
                    }
                }
            }


            /* FILTRO ESTADO */

            Select({

                classes(AppStyles.filterSelect)

                onChange {

                    selectedEstado = it.target.value
                }

            }) {

                Option("todos") {

                    Text("Todos los estados")
                }

                Option("pendiente") {

                    Text("Pendientes")
                }

                Option("aprobado") {

                    Text("Aprobadas")
                }

                Option("rechazado") {

                    Text("Rechazadas")
                }
            }


            /* BOTÓN CREAR VACACIÓN */

            Button({

                classes(AppStyles.primaryButton)

                onClick {

                    showCreateDialog = true
                }

            }) {

                Text("+ Nueva vacación")
            }
        }

        Div({

            classes(AppStyles.statsGrid)

        }) {

            StatCard(
                title = "Navidad restantes",
                value = totalNavidadRestantes.toString(),
                accent = StatAccent.BLUE
            )

            StatCard(
                title = "Libres restantes",
                value = totalLibresRestantes.toString(),
                accent = StatAccent.YELLOW
            )

            StatCard(
                title = "Total disponibles",
                value = totalDiasRestantes.toString(),
                accent = StatAccent.GREEN
            )

            StatCard(
                title = "Pendientes aprobar",
                value = pendientesAprobacion.toString(),
                accent = StatAccent.RED
            )
        }


        /* TABLA */

        if (loading) {

            Div({

                classes(AppStyles.loaderContainer)

            }) {

                Div({

                    classes(AppStyles.loader)

                }) {}
            }

        } else {

            Div({

                classes(AppStyles.tableContainer)

            }) {

                Table({

                    classes(AppStyles.tableFlat)

                }) {

                    Thead {

                        Tr {

                            Th { Text("ID") }

                            Th { Text("Usuario") }

                            Th { Text("Inicio") }

                            Th { Text("Fin") }

                            Th { Text("Estado") }

                            Th { Text("Acción") }
                        }
                    }


                    Tbody {

                        vacacionesFiltradas.forEach { vacacion ->

                            val rowStyle = when (vacacion.estado) {

                                "aprobado" -> AppStyles.rowApproved

                                "rechazado" -> AppStyles.rowRejected

                                else -> null
                            }


                            Tr({

                                if (rowStyle != null) {

                                    classes(rowStyle)
                                }

                            }) {

                                Td {

                                    Text("${vacacion.id}")
                                }


                                Td {

                                    Text(vacacion.username)
                                }


                                Td {

                                    Text(vacacion.fechaInicio)
                                }


                                Td {

                                    Text(vacacion.fechaFin)
                                }


                                Td {

                                    val badgeClass = when (vacacion.estado) {

                                        "aprobado" ->
                                            AppStyles.badgeAprobado

                                        "rechazado" ->
                                            AppStyles.badgeRechazado

                                        else ->
                                            AppStyles.badgePendiente
                                    }


                                    Span({

                                        classes(badgeClass)

                                    }) {

                                        Text(vacacion.estado.replaceFirstChar { it.uppercase() })
                                    }
                                }


                                Td({

                                    classes(AppStyles.actionCell)

                                }) {

                                    if (vacacion.estado == "pendiente") {

                                        Div({

                                            classes(AppStyles.actionsGroup)

                                        }) {

                                            Button({

                                                classes(AppStyles.successButton)

                                                onClick {

                                                    selectedVacacionId = vacacion.id

                                                    selectedUsername = vacacion.username

                                                    nuevoEstado = "aprobado"

                                                    showDialog = true
                                                }

                                            }) {

                                                Img("/icons/check.svg"){
                                                    classes(AppStyles.aproveIcon)
                                                }
                                            }


                                            Button({

                                                classes(AppStyles.dangerButton)

                                                onClick {

                                                    selectedVacacionId = vacacion.id

                                                    selectedUsername = vacacion.username

                                                    nuevoEstado = "rechazado"

                                                    showDialog = true
                                                }

                                            }) {

                                                Img("/icons/cross.svg"){
                                                    classes(AppStyles.desAproveIcon)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (resumenGlobal.isNotEmpty()) {

                    H3 {

                        Text("Resumen por usuario")
                    }

                    Div({

                        classes(AppStyles.tableContainer)

                    }) {

                        Table({

                            classes(AppStyles.tableCard)

                        }) {

                            Thead {

                                Tr {

                                    Th { Text("Usuario") }

                                    Th { Text("Navidad restantes") }

                                    Th { Text("Libres restantes") }

                                    Th { Text("Total disponibles") }
                                }
                            }

                            Tbody {

                                resumenGlobal.forEach {

                                    val highlightClass = when {

                                        it.diasTotalesRestantes == 0 ->
                                            AppStyles.rowRejected

                                        it.diasTotalesRestantes <= 2 ->
                                            AppStyles.rowWarning

                                        else ->
                                            AppStyles.rowApproved
                                    }

                                    Tr({

                                        classes(highlightClass)

                                    }) {

                                        Td { Text(it.username) }

                                        Td { Text("${it.diasNavidadRestantes}") }

                                        Td { Text("${it.diasLibresRestantes}") }

                                        Td { Text("${it.diasTotalesRestantes}") }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        /* CONFIRM DIALOG */

        if (showDialog && selectedVacacionId != null) {

            ConfirmDialog(

                message =

                    if (nuevoEstado == "aprobado")

                        "¿Seguro que deseas aprobar estas vacaciones?"

                    else

                        "¿Seguro que deseas rechazar estas vacaciones?",

                confirmText =

                    if (nuevoEstado == "aprobado")

                        "Aprobar"

                    else

                        "Rechazar",

                confirmClass =

                    if (nuevoEstado == "aprobado")

                        AppStyles.approveButton

                    else

                        AppStyles.rejectButton,

                onConfirm = {

                    scope.launch {

                        actualizarEstado(
                            selectedVacacionId!!,
                            nuevoEstado
                        )

                        toastMessage =
                            if (nuevoEstado == "aprobado")
                                "Vacaciones aprobadas para $selectedUsername"
                            else
                                "Vacaciones rechazadas para $selectedUsername"

                        toastType =
                            if (nuevoEstado == "aprobado")
                                "success"
                            else
                                "error"

                        showDialog = false
                    }
                },

                onCancel = {

                    showDialog = false
                }
            )
        }
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

