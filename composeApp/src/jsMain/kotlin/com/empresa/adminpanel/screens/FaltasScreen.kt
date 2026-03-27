package com.empresa.adminpanel.screens

import androidx.compose.runtime.*
import com.empresa.adminpanel.models.Falta
import com.empresa.adminpanel.models.Usuario
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.dom.*
import org.w3c.fetch.Headers
import style.AppStyles

import com.empresa.adminpanel.components.ConfirmDialog
import com.empresa.adminpanel.components.CreateFaltaDialog
import org.jetbrains.compose.web.css.*


@Composable
fun FaltasScreen() {

    var faltas by remember {
        mutableStateOf<List<Falta>>(emptyList())
    }

    var usuarios by remember {
        mutableStateOf<List<Usuario>>(emptyList())
    }

    var selectedUserId: String? by remember {
        mutableStateOf("todos")
    }

    var selectedTipo: String? by remember {
        mutableStateOf("todos")
    }

    var selectedFaltaId by remember {
        mutableStateOf<Int?>(null)
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    var loading by remember {
        mutableStateOf(true)
    }

    var showCreateDialog by remember {
        mutableStateOf(false)
    }



    val scope = rememberCoroutineScope()


    fun cargarFaltas() {

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
                "http://127.0.0.1:8080/faltas",
                requestInit
            ).await()

            if (response.ok) {

                val text = response.text().await()

                faltas =
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

                usuarios =
                    Json.decodeFromString(text)
            }
        }
    }

    fun eliminarFalta(id: Int) {

        scope.launch {

            val token = window.localStorage.getItem("token")
                ?: return@launch

            val headers = Headers()

            headers.append(
                "Authorization",
                "Bearer $token"
            )

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

    fun registrarFalta(
        userId: Int,
        fecha: String,
        tipo: String,
        descripcion: String
    ) {

        scope.launch {

            val token = window.localStorage.getItem("token")
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

            val bodyObject = js("{}")

            bodyObject.fecha = fecha
            bodyObject.tipo = tipo
            bodyObject.descripcion = descripcion

            val body = JSON.stringify(bodyObject)

            val requestInit = js("{}")

            requestInit.method = "POST"
            requestInit.headers = headers
            requestInit.body = body

            window.fetch(
                "http://127.0.0.1:8080/faltas/$userId",
                requestInit
            ).await()

            cargarFaltas()
        }
    }


    LaunchedEffect(Unit) {
        cargarFaltas()
        cargarUsuarios()
    }


    val faltasFiltradas = faltas.filter {

        (selectedUserId == "todos"
                || it.userId.toString() == selectedUserId)

                &&

                (selectedTipo == "todos"
                        || it.tipo == selectedTipo)
    }

    if (showCreateDialog) {

        CreateFaltaDialog(

            usuarios = usuarios,

            onConfirm = {

                    userId,
                    fecha,
                    tipo,
                    descripcion ->

                registrarFalta(
                    userId,
                    fecha,
                    tipo,
                    descripcion
                )

                showCreateDialog = false
            },

            onCancel = {

                showCreateDialog = false
            }
        )
    }


    Div {

        /* HEADER */

        Div({

            style {

                display(DisplayStyle.Flex)

                justifyContent(JustifyContent.SpaceBetween)

                alignItems(AlignItems.Center)

                marginBottom(28.px)
            }

        }) {

            /* IZQUIERDA: título + recargar */

            Div({

                style {

                    display(DisplayStyle.Flex)

                    alignItems(AlignItems.Center)

                    gap(12.px)
                }

            }) {

                H2({

                    classes(AppStyles.title)

                }) {

                    Text("Gestión de faltas")
                }


                Button({

                    classes(AppStyles.secondaryButton)

                    onClick {

                        cargarFaltas()
                    }

                }) {

                    Img(
                        src = "/icons/refresh.svg",
                        attrs = {
                            classes(AppStyles.buttonIcon)
                        }
                    )

                    Text("Recargar")
                }
            }


            /* DERECHA: nueva falta */

            Button({

                classes(AppStyles.primaryButton)

                onClick {

                    showCreateDialog = true
                }

            }) {

                Img(
                    src = "/icons/add.svg",
                    attrs = {
                        classes(AppStyles.buttonIcon)
                    }
                )

                Text("Nueva falta")
            }
        }


        /* FILTROS */

        Div({

            style {

                display(DisplayStyle.Flex)

                gap(16.px)

                marginBottom(20.px)
            }

        }) {

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


            Select({

                classes(AppStyles.filterSelect)

                onChange {
                    selectedTipo = it.target.value
                }

            }) {

                Option("todos") {
                    Text("Todos los tipos")
                }

                Option("retraso") {
                    Text("Retraso")
                }

                Option("justificada") {
                    Text("Justificada")
                }

                Option("injustificada") {
                    Text("Injustificada")
                }
            }
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

                            Th { Text("Tipo") }

                            Th { Text("Descripción") }

                            Th { Text("Acción") }
                        }
                    }


                    Tbody {

                        faltasFiltradas.forEach { falta ->

                            val badgeStyle = when (falta.tipo) {

                                "retraso" ->
                                    AppStyles.badgeWarning

                                "injustificada" ->
                                    AppStyles.badgeDanger

                                else ->
                                    AppStyles.badgeInfo
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

                                            selectedFaltaId =
                                                falta.id

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


        /* DIALOGO CONFIRMACION */

        if (showDialog && selectedFaltaId != null) {

            ConfirmDialog(
                message = "¿Eliminar falta?",

                confirmText = "Eliminar",

                confirmClass = AppStyles.deleteButton,

                onConfirm = {

                    scope.launch {

                        eliminarFalta(selectedFaltaId!!)

                        cargarFaltas()

                        showDialog = false
                    }
                },

                onCancel = {

                    showDialog = false
                }
            )
        }
    }
}