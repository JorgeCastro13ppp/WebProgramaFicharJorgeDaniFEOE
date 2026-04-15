package com.empresa.adminpanel.screens

import androidx.compose.runtime.*
import com.empresa.adminpanel.components.*
import com.empresa.adminpanel.models.Falta
import com.empresa.adminpanel.models.Usuario
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.css.*
import org.w3c.fetch.Headers
import style.AppStyles


@Composable
fun FaltasScreen() {

    var faltas by remember { mutableStateOf<List<Falta>>(emptyList()) }

    var usuarios by remember { mutableStateOf<List<Usuario>>(emptyList()) }

    var selectedUserId by remember { mutableStateOf("todos") }

    var selectedTipo by remember { mutableStateOf("todos") }

    var selectedFaltaId by remember { mutableStateOf<Int?>(null) }

    var showDialog by remember { mutableStateOf(false) }

    var showCreateDialog by remember { mutableStateOf(false) }

    var loading by remember { mutableStateOf(true) }

    var toastMessage by remember { mutableStateOf<String?>(null) }

    var toastType by remember { mutableStateOf("success") }

    var selectedUsername by remember { mutableStateOf<String?>(null) }

    var selectedTipoFalta by remember { mutableStateOf<String?>(null) }

    var selectedFechaFalta by remember { mutableStateOf<String?>(null) }

    var sortBy by remember { mutableStateOf("fecha") }
    var order by remember { mutableStateOf("desc") }

    val scope = rememberCoroutineScope()


    /* =========================
       CARGAR FALTAS
       ========================= */

    fun cargarFaltas() {

        scope.launch {

            loading = true

            val token =
                window.localStorage.getItem("token")
                    ?: return@launch

            val headers = Headers()

            headers.append("Authorization", "Bearer $token")


            val params = mutableListOf<String>()


            if (selectedTipo != "todos") {

                params.add("tipo=$selectedTipo")
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
                "http://127.0.0.1:8080/faltas$queryString"


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

                faltas =
                    Json.decodeFromString(text)
            }

            loading = false
        }
    }


    /* =========================
       CARGAR USUARIOS
       ========================= */

    fun cargarUsuarios() {

        scope.launch {

            val token =
                window.localStorage.getItem("token")
                    ?: return@launch

            val headers = Headers()

            headers.append("Authorization", "Bearer $token")

            val requestInit = js("{}")

            requestInit.method = "GET"
            requestInit.headers = headers

            val response =
                window.fetch(
                    "http://127.0.0.1:8080/admin/usuarios",
                    requestInit
                ).await()

            if (response.ok) {

                val text = response.text().await()

                usuarios =
                    Json.decodeFromString(text)
            }
        }
    }


    /* =========================
       ELIMINAR FALTA
       ========================= */

    fun eliminarFalta(id: Int) {

        scope.launch {

            val token =
                window.localStorage.getItem("token")
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


    /* =========================
       INIT LOAD
       ========================= */

    LaunchedEffect(Unit) {

        cargarFaltas()
        cargarUsuarios()
    }


    /* =========================
       CREATE DIALOG
       ========================= */

    if (showCreateDialog) {

        CreateFaltaDialog(

            usuarios = usuarios,

            onConfirm = { userId, fecha, tipo, _ ->

                val username =
                    usuarios.find { it.id == userId }
                        ?.username ?: "usuario"

                toastMessage =
                    "Falta registrada: $tipo · $username"

                toastType = "success"

                showCreateDialog = false

                cargarFaltas()
            },

            onError = { mensaje ->

                showCreateDialog = false

                toastMessage = mensaje

                toastType = "warning"
            },

            onCancel = {

                showCreateDialog = false
            }
        )
    }


    /* =========================
       UI
       ========================= */

    Div {

        ScreenHeader(

            title = "Gestión de faltas",

            onRefresh = {

                cargarFaltas()
            }

        ) {

            Select({

                classes(AppStyles.filterSelect)

                onChange {

                    sortBy = it.target.value

                    cargarFaltas()
                }

            }) {

                Option("fecha") {

                    Text("Fecha")
                }

                Option("usuario") {

                    Text("Usuario")
                }

                Option("tipo") {

                    Text("Tipo")
                }

                Option("id") {

                    Text("ID")
                }
            }

            Select({

                classes(AppStyles.filterSelect)

                onChange {

                    order = it.target.value

                    cargarFaltas()
                }

            }) {

                Option("desc") {

                    Text("Descendente")
                }

                Option("asc") {

                    Text("Ascendente")
                }
            }

            Select({

                classes(AppStyles.filterSelect)

                onChange {

                    selectedUserId = it.target.value
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


            Select({

                classes(AppStyles.filterSelect)

                onChange {

                    selectedTipo = it.target.value
                }

            }) {

                Option("todos") { Text("Todos los tipos") }

                Option("retraso") { Text("Retraso") }

                Option("justificada") { Text("Justificada") }

                Option("injustificada") { Text("Injustificada") }
            }


            Button({

                classes(AppStyles.primaryButton)

                onClick {

                    showCreateDialog = true
                }

            }) {

                Text("+ Nueva falta")
            }
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

            val faltasFiltradas = faltas.filter {

                (selectedUserId == "todos"
                        || it.userId.toString() == selectedUserId)

                        &&

                        (selectedTipo == "todos"
                                || it.tipo == selectedTipo)
            }


            Div({

                classes(
                    AppStyles.tableContainer,
                    AppStyles.tableWrapperCentered
                )

            }) {

                Table({

                    classes(AppStyles.table)

                }) {

                    Thead {

                        Tr {

                            Th { Text("ID") }

                            Th { Text("Usuario") }

                            Th { Text("Fecha") }

                            Th { Text("Tipo") }

                            Th { Text("Descripción") }

                            Th { Text("Acción") }
                        }
                    }


                    Tbody {

                        faltasFiltradas.forEach { falta ->

                            val badgeStyle = when (falta.tipo) {

                                "retraso" -> AppStyles.badgeWarning

                                "injustificada" -> AppStyles.badgeDanger

                                else -> AppStyles.badgeInfo
                            }

                            Tr {

                                Td { Text("${falta.id}") }

                                Td { Text(falta.username) }

                                Td { Text(falta.fecha) }

                                Td {

                                    Span({

                                        classes(
                                            AppStyles.badgeFichar,
                                            badgeStyle
                                        )

                                    }) {

                                        Text(falta.tipo)
                                    }
                                }

                                Td {

                                    Text(falta.descripcion)
                                }

                                Td {

                                    Button({

                                        classes(AppStyles.deleteButton)

                                        onClick {

                                            selectedFaltaId = falta.id

                                            selectedUsername =
                                                falta.username

                                            selectedTipoFalta =
                                                falta.tipo

                                            selectedFechaFalta =
                                                falta.fecha

                                            showDialog = true
                                        }

                                    }) {

                                        Img(
                                            src = "/icons/delete.svg",
                                            attrs = {
                                                classes(AppStyles.deleteIcon)
                                            }
                                        )

                                        Text("Eliminar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        /* =========================
           DELETE CONFIRM DIALOG
           ========================= */

        if (showDialog && selectedFaltaId != null) {

            ConfirmDialog(

                message = """
¿Eliminar falta?

Usuario: $selectedUsername
Tipo: $selectedTipoFalta
Fecha: $selectedFechaFalta
""".trimIndent(),

                confirmText = "Eliminar",

                confirmClass = AppStyles.deleteButton,

                onConfirm = {

                    eliminarFalta(selectedFaltaId!!)

                    toastMessage =
                        "Falta eliminada: $selectedTipoFalta · $selectedUsername"

                    toastType = "error"

                    showDialog = false
                },

                onCancel = {

                    showDialog = false
                }
            )
        }
    }


    /* =========================
       TOAST
       ========================= */

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