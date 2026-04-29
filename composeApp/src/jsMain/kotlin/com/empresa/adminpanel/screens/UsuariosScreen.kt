package com.empresa.adminpanel.screens

import androidx.compose.runtime.*
import com.empresa.adminpanel.ApiClient
import com.empresa.adminpanel.components.ConfirmDialog
import com.empresa.adminpanel.components.CreateUserDialog
import com.empresa.adminpanel.components.LoadingSpinner
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

    var selectedUserId by remember {
        mutableStateOf<Int?>(null)
    }

    var selectedUsername by remember {
        mutableStateOf<String?>(null)
    }

    var deleting by remember { mutableStateOf(false) }

    var showCreateDialog by remember {
        mutableStateOf(false)
    }

    var toastMessage by remember {
        mutableStateOf<String?>(null)
    }

    var toastType by remember {
        mutableStateOf("success")
    }

    var sortBy by remember {
        mutableStateOf("username")
    }

    var order by remember {
        mutableStateOf("asc")
    }

    var loading by remember {
        mutableStateOf(true)
    }

    val scope = rememberCoroutineScope()


    /*
    ========================
    LOAD USERS
    ========================
    */

    fun cargarUsuarios() {

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


            if (selectedRole != "todos")
                params.add("role=$selectedRole")

            params.add("sortBy=$sortBy")

            params.add("order=$order")


            val queryString =
                if (params.isNotEmpty())
                    "?" + params.joinToString("&")
                else
                    ""


            val requestInit = js("{}")

            requestInit["method"] = "GET"
            requestInit["headers"] = headers

            val response =
                window.fetch(
                    "${ApiClient.BASE_URL}/admin/usuarios$queryString",
                    requestInit
                ).await()


            if (response.ok) {

                usuarios =
                    Json.decodeFromString(
                        response.text().await()
                    )
            }

            loading = false
        }
    }


    suspend fun eliminarUsuario(id: Int) {

        val token =
            window.localStorage.getItem("token")
                ?: return

        val headers = Headers()

        headers.append(
            "Authorization",
            "Bearer $token"
        )

        val requestInit = js("{}")

        requestInit["method"] = "DELETE"
        requestInit["headers"] = headers

        window.fetch(
            "\${ApiConfig.BASE_URL}/admin/usuarios/$id",
            requestInit
        ).await()
    }


    LaunchedEffect(Unit) {

        cargarUsuarios()
    }


    /*
    ========================
    UI
    ========================
    */

    Div({

        classes(AppStyles.screenContainer)

    }) {

        /*
        ========================
        HEADER
        ========================
        */

        ScreenHeader(

            title = "Usuarios registrados",

            onRefresh = {

                cargarUsuarios()
            }

        ) {

            Div({

                classes(AppStyles.filtersRow)

            }) {

                Select({

                    classes(AppStyles.filterSelect)

                    onChange {

                        sortBy = it.target.value

                        cargarUsuarios()
                    }

                }) {

                    Option("username") {
                        Text("Usuario")
                    }

                    Option("role") {
                        Text("Rol")
                    }

                    Option("id") {
                        Text("ID")
                    }
                }


                Select({

                    classes(AppStyles.filterSelect)

                    onChange {

                        order = it.target.value

                        cargarUsuarios()
                    }

                }) {

                    Option("asc") {
                        Text("Ascendente")
                    }

                    Option("desc") {
                        Text("Descendente")
                    }
                }


                Select({

                    classes(AppStyles.filterSelect)

                    onChange {

                        selectedRole = it.target.value

                        cargarUsuarios()
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


                Button({

                    classes(AppStyles.primaryButton)

                    onClick {

                        showCreateDialog = true
                    }

                }) {

                    Text("+ Nuevo usuario")
                }
            }
        }


        /*
        ========================
        LOADING
        ========================
        */

        if (loading) {

            LoadingSpinner()

            return@Div
        }


        /*
        ========================
        EMPTY STATE
        ========================
        */

        if (usuarios.isEmpty()) {

            P({

                classes(AppStyles.emptyState)

            }) {

                Text("No hay usuarios registrados")
            }

            return@Div
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

                classes(AppStyles.tableCard)

            }) {

                Thead {

                    Tr {

                        Th { Text("ID") }

                        Th { Text("Usuario") }

                        Th { Text("Rol") }

                        Th { Text("Acción") }
                    }
                }


                Tbody {

                    usuarios.forEach { usuario ->

                        val roleStyle =
                            if (usuario.role == "admin")

                                AppStyles.roleAdmin

                            else

                                AppStyles.roleWorker


                        Tr {

                            Td {

                                Text("${usuario.id}")
                            }


                            Td {

                                Text(usuario.username)
                            }


                            Td {

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
                                classes(AppStyles.actionCell)
                            }

                            ) {

                                Button({

                                    classes(AppStyles.deleteButton)

                                    onClick {

                                        selectedUserId =
                                            usuario.id

                                        selectedUsername =
                                            usuario.username

                                        showDialog = true
                                    }

                                }) {

                                    Img("/icons/delete.svg"){
                                        classes(AppStyles.deleteIcon)
                                    }
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }


        /*
        ========================
        DELETE CONFIRM
        ========================
        */

        if (showDialog && selectedUserId != null) {

            ConfirmDialog(

                message =
                    "¿Eliminar usuario \"$selectedUsername\"?",

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

                        deleting = false

                        showDialog = false
                    }
                },

                onCancel = {

                    showDialog = false
                }
            )
        }


        /*
        ========================
        CREATE USER
        ========================
        */

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


    /*
    ========================
    TOAST
    ========================
    */

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