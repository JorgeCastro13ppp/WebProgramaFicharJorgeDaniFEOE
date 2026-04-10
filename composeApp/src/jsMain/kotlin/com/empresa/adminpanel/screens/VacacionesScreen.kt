package com.empresa.adminpanel.screens

import androidx.compose.runtime.*
import com.empresa.adminpanel.components.ConfirmDialog
import com.empresa.adminpanel.components.CreateVacacionDialog
import com.empresa.adminpanel.components.ScreenHeader
import com.empresa.adminpanel.components.Toast
import com.empresa.adminpanel.models.Usuario
import com.empresa.adminpanel.models.Vacacion
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

    val scope = rememberCoroutineScope()

    fun cargarVacaciones() {

        scope.launch {

            loading = true

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
                "http://127.0.0.1:8080/vacaciones",
                requestInit
            ).await()

            if (response.ok) {

                val text = response.text().await()

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
                "http://127.0.0.1:8080/admin/usuarios",
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
                "http://127.0.0.1:8080/vacaciones/$id?estado=$estado",
                requestInit
            ).await()

            cargarVacaciones()
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
    }

    Div {

        ScreenHeader(

            title = "Gestión de vacaciones",

            onRefresh = {

                cargarVacaciones()
            }

        ) {

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

                    classes(AppStyles.table)

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

                                            classes(AppStyles.actionButtonsGroup)

                                        }) {

                                            Button({

                                                classes(AppStyles.approveButton)

                                                onClick {

                                                    selectedVacacionId = vacacion.id

                                                    selectedUsername = vacacion.username

                                                    nuevoEstado = "aprobado"

                                                    showDialog = true
                                                }

                                            }) {

                                                Text("Aprobar")
                                            }


                                            Button({

                                                classes(AppStyles.rejectButton)

                                                onClick {

                                                    selectedVacacionId = vacacion.id

                                                    selectedUsername = vacacion.username

                                                    nuevoEstado = "rechazado"

                                                    showDialog = true
                                                }

                                            }) {

                                                Text("Rechazar")
                                            }
                                        }
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

