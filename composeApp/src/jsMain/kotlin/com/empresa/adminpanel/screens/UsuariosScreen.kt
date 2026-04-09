package com.empresa.adminpanel.screens

import androidx.compose.runtime.*
import com.empresa.adminpanel.components.ConfirmDialog
import com.empresa.adminpanel.components.CreateUserDialog
import com.empresa.adminpanel.components.ScreenHeader
import com.empresa.adminpanel.components.Toast
import com.empresa.adminpanel.models.Usuario
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.w3c.fetch.Headers
import style.AppStyles

@Composable
fun UsuariosScreen() {

    var usuarios by remember {
        mutableStateOf<List<Usuario>>(emptyList())
    }
    var selectedRole by remember {
        mutableStateOf("todos")
    }

    var showDialog by remember { mutableStateOf(false) }
    var selectedUserId by remember { mutableStateOf<Int?>(null) }
    var selectedUsername by remember {
        mutableStateOf<String?>(null)
    }
    var deleting by remember { mutableStateOf(false) }

    var showCreateDialog by remember { mutableStateOf(false) }
    var showToast by remember { mutableStateOf(false) }

    var toastMessage by remember {
        mutableStateOf<String?>(null)
    }

    var toastType by remember {
        mutableStateOf("success")
    }
    val scope = rememberCoroutineScope()

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
                    Json.decodeFromString<List<Usuario>>(text)
            }
        }
    }

    suspend fun eliminarUsuario(id: Int) {

        val token = window.localStorage.getItem("token")
            ?: return

        val headers = Headers()
        headers.append("Authorization", "Bearer $token")

        val requestInit = js("{}")
        requestInit.method = "DELETE"
        requestInit.headers = headers

        window.fetch(
            "http://127.0.0.1:8080/admin/usuarios/$id",
            requestInit
        ).await()
    }

    LaunchedEffect(Unit) {
        cargarUsuarios()
    }

    Div {

        ScreenHeader(

            title = "Usuarios registrados",

            onRefresh = {

                cargarUsuarios()
            }

        ) {

            /* FILTRO ROLE */

            Select({

                classes(AppStyles.filterSelect)

                onChange {

                    selectedRole = it.target.value
                }

            }) {

                Option("todos") {

                    Text("Todos los roles")
                }

                Option("admin") {

                    Text("Admin")
                }

                Option("worker") {

                    Text("Empleado")
                }
            }


            /* BOTÓN NUEVO USUARIO */

            Button({

                classes(AppStyles.primaryButton)

                onClick {

                    showCreateDialog = true
                }

            }) {

                Text("+ Nuevo usuario")
            }
        }

        /* TABLA */

        Div({
            classes(AppStyles.tableContainer)
        }) {

            Table({
                classes(AppStyles.table)
            }) {

                Thead {

                    Tr({
                        classes(AppStyles.tableHeaderRow)
                    }) {

                        Th({ classes(AppStyles.tableHeader) }) {
                            Text("ID")
                        }

                        Th({ classes(AppStyles.tableHeader) }) {
                            Text("Usuario")
                        }

                        Th({ classes(AppStyles.tableHeader) }) {
                            Text("Rol")
                        }

                        Th({ classes(AppStyles.tableHeader) }) {
                            Text("Acción")
                        }
                    }
                }

                Tbody {
                    val usuariosFiltrados = usuarios.filter {

                        selectedRole == "todos" ||

                                it.role == selectedRole
                    }

                    usuariosFiltrados.forEach { usuario ->

                        Tr({
                            classes(AppStyles.tableRow)
                        }) {

                            Td({
                                classes(AppStyles.tableCell)
                            }) {
                                Text("${usuario.id}")
                            }

                            Td({
                                classes(AppStyles.tableCell)
                            }) {
                                Text(usuario.username)
                            }

                            Td({
                                classes(AppStyles.tableCell)
                            }) {

                                val roleStyle =
                                    if (usuario.role == "admin")
                                        AppStyles.roleAdmin
                                    else
                                        AppStyles.roleWorker

                                Span({
                                    classes(
                                        AppStyles.roleBadge,
                                        roleStyle
                                    )
                                }) {
                                    Text(usuario.role)
                                }
                            }

                            Td({
                                classes(AppStyles.tableCell)
                            }) {

                                Button(attrs = {

                                    classes(AppStyles.deleteButton)

                                    onClick {
                                        selectedUserId = usuario.id
                                        selectedUsername = usuario.username
                                        showDialog = true
                                    }

                                }) {

                                    if (
                                        deleting &&
                                        selectedUserId == usuario.id
                                    ) {

                                        Div({
                                            classes(AppStyles.loader)
                                        }) {}

                                    } else {

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

        /* DIALOG ELIMINAR */

        if (showDialog && selectedUserId != null) {

            ConfirmDialog(
                message = "¿Eliminar usuario \"$selectedUsername\"?"
                ,

                confirmText = "Eliminar",

                confirmClass = AppStyles.deleteButton,

                onConfirm = {

                    deleting = true

                    scope.launch {

                        eliminarUsuario(selectedUserId!!)

                        toastMessage =
                            "Usuario \"$selectedUsername\" eliminado correctamente"

                        toastType = "error"

                        cargarUsuarios()

                        cargarUsuarios()

                        deleting = false
                        showDialog = false
                    }
                },

                onCancel = {

                    showDialog = false
                }
            )
        }

        /* DIALOG CREAR USUARIO */

        if (showCreateDialog) {

            CreateUserDialog(

                onClose = {

                    showCreateDialog = false
                },

                onCreated = { username ->

                    cargarUsuarios()

                    toastMessage =
                        "Usuario \"$username\" creado correctamente"

                    toastType = "success"
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