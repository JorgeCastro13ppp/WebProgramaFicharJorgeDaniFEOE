package com.empresa.adminpanel.screens

import androidx.compose.runtime.*
import com.empresa.adminpanel.components.ConfirmDialog
import com.empresa.adminpanel.components.CreateFichajeDialog
import com.empresa.adminpanel.components.ScreenHeader
import com.empresa.adminpanel.components.Toast
import com.empresa.adminpanel.models.Fichaje
import com.empresa.adminpanel.models.Usuario
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.w3c.fetch.Headers
import style.AppStyles
import kotlin.js.Date

@Composable
fun FichajesScreen() {

    var fichajes by remember { mutableStateOf<List<Fichaje>>(emptyList()) }
    var usuarios by remember { mutableStateOf<List<Usuario>>(emptyList()) }

    var selectedUserId by remember { mutableStateOf<String>("todos") }

    var loading by remember { mutableStateOf(true) }

    var showDialog by remember { mutableStateOf(false) }
    var selectedFichajeId by remember { mutableStateOf<Int?>(null) }

    var filtroTipo by remember {
        mutableStateOf("todos")
    }

    var showCreateDialog by remember {
        mutableStateOf(false)
    }

    var filtroFecha by remember { mutableStateOf("") }

    var toastMessage by remember {
        mutableStateOf<String?>(null)
    }

    var toastType by remember {
        mutableStateOf("success")
    }

    var selectedTipoFichaje by remember {
        mutableStateOf<String?>(null)
    }

    val scope = rememberCoroutineScope()

    fun badgeClass(tipo: String): String {

        return when {

            tipo.contains("entrada") ->
                AppStyles.badgeGreen

            tipo.contains("salida") ->
                AppStyles.badgeRed

            tipo.contains("viaje") ->
                AppStyles.badgeBlue

            tipo.contains("descanso") ->
                AppStyles.badgeOrange

            else ->
                AppStyles.badgeDefault
        }
    }

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


    fun cargarFichajes() {

        scope.launch {

            loading = true

            val token =
                window.localStorage.getItem("token")
                    ?: return@launch

            val headers = Headers()

            headers.append("Authorization", "Bearer $token")

            val requestInit = js("{}")

            requestInit.method = "GET"
            requestInit.headers = headers

            val url =
                if (selectedUserId == "todos")
                    "http://127.0.0.1:8080/admin/fichajes"
                else
                    "http://127.0.0.1:8080/admin/fichajes?userId=$selectedUserId"

            val response =
                window.fetch(url, requestInit)
                    .await()

            if (response.ok) {

                val text = response.text().await()

                fichajes =
                    Json.decodeFromString(text)
            }

            loading = false
        }
    }


    suspend fun eliminarFichaje(id: Int) {

        val token =
            window.localStorage.getItem("token")
                ?: return

        val headers = Headers()

        headers.append("Authorization", "Bearer $token")

        val requestInit = js("{}")

        requestInit.method = "DELETE"
        requestInit.headers = headers

        window.fetch(
            "http://127.0.0.1:8080/admin/fichajes/$id",
            requestInit
        ).await()
    }

    if (showCreateDialog) {

        CreateFichajeDialog(

            usuarios = usuarios,

            onClose = {

                showCreateDialog = false
            },

            onCreated = { accion, contexto ->

                cargarFichajes()

                toastMessage =
                    "Fichaje registrado: $accion en $contexto"

                toastType = "success"
            }
        )
    }


    LaunchedEffect(Unit) {

        cargarUsuarios()

        cargarFichajes()
    }


    LaunchedEffect(selectedUserId) {

        cargarFichajes()
    }


    /* HEADER */

    Div {

        ScreenHeader(

            title = "Fichajes",

            onRefresh = {

                cargarFichajes()
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


            /* FILTRO TIPO */

            Select({

                classes(AppStyles.filterSelect)

                onChange {

                    filtroTipo = it.target.value
                }

            }) {

                Option("todos") { Text("Todos") }
                Option("entrada") { Text("Entradas") }
                Option("salida") { Text("Salidas") }
                Option("viaje") { Text("Viajes") }
                Option("descanso") { Text("Descansos") }
            }


            /* FILTRO FECHA */

            Input(InputType.Date, attrs = {

                classes(AppStyles.filterSelect)

                value(filtroFecha)

                onInput {

                    filtroFecha = it.value
                }
            })


            /* BOTÓN CREAR */

            Button({

                classes(AppStyles.primaryButton)

                onClick {

                    showCreateDialog = true
                }

            }) {

                Text("+ Nuevo fichaje")
            }
        }


        /* FILTROS APLICADOS */

        val fichajesFiltrados = fichajes
            .filter {

                selectedUserId == "todos" ||
                        it.userId.toString() == selectedUserId
            }
            .filter {

                filtroTipo == "todos" ||
                        it.tipo.contains(filtroTipo)
            }
            .filter {

                filtroFecha.isEmpty() ||

                        Date(it.fechaHora)
                            .toISOString()
                            .substring(0, 10) == filtroFecha
            }


        /* LOADER */

        if (loading) {

            Div({

                classes(AppStyles.loaderContainer)

            }) {

                Div({

                    classes(AppStyles.loader)

                }) {}
            }

        } else {

            /* TABLA */

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
                            Th { Text("Hora") }
                            Th { Text("Tipo") }
                            Th { Text("Acción") }
                        }
                    }


                    Tbody {

                        fichajesFiltrados.forEach { fichaje ->

                            val fecha =
                                Date(fichaje.fechaHora)
                                    .toLocaleDateString()

                            val hora =
                                Date(fichaje.fechaHora)
                                    .toLocaleTimeString()


                            Tr {

                                Td { Text("${fichaje.id}") }

                                Td { Text(fichaje.username) }

                                Td { Text(fecha) }

                                Td { Text(hora) }


                                Td {

                                    Span({

                                        classes(
                                            badgeClass(
                                                fichaje.tipo
                                            )
                                        )

                                    }) {

                                        Text(fichaje.tipo)
                                    }
                                }


                                Td {

                                    Button({

                                        classes(AppStyles.deleteButton)

                                        onClick {

                                            selectedFichajeId = fichaje.id
                                            selectedTipoFichaje = fichaje.tipo

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


        /* DIALOGO CONFIRMACIÓN */

        if (showDialog && selectedFichajeId != null) {

            ConfirmDialog(

                message = "¿Eliminar fichaje?",

                confirmText = "Eliminar",

                confirmClass = AppStyles.deleteButton,

                onConfirm = {

                    scope.launch {

                        eliminarFichaje(selectedFichajeId!!)

                        toastMessage =
                            "Fichaje eliminado: $selectedTipoFichaje"

                        toastType = "error"

                        cargarFichajes()

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