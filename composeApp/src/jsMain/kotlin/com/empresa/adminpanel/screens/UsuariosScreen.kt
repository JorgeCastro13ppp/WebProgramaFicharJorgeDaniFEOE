package com.empresa.adminpanel.screens

import androidx.compose.runtime.*
import com.empresa.adminpanel.components.ConfirmDialog
import com.empresa.adminpanel.components.CreateUserDialog
import com.empresa.adminpanel.components.ToastMessage
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

    var showDialog by remember { mutableStateOf(false) }
    var selectedUserId by remember { mutableStateOf<Int?>(null) }
    var deleting by remember { mutableStateOf(false) }

    var showCreateDialog by remember { mutableStateOf(false) }
    var showToast by remember { mutableStateOf(false) }

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

        /* HEADER + BOTÓN CREAR */

        Div({

            style {
                display(DisplayStyle.Flex)
                justifyContent(JustifyContent.SpaceBetween)
                alignItems(AlignItems.Center)
                marginBottom(20.px)
            }

        }) {

            H2({
                classes(AppStyles.title)
            }) {
                Text("Usuarios registrados")
            }

            Button({

                classes(AppStyles.addButton)

                onClick {
                    showCreateDialog = true
                }

            }) {
                Img(
                    src = "/icons/add.svg",
                    attrs = {
                        classes(AppStyles.addIcon)
                    }
                )
                Text("Nuevo usuario")
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

                    usuarios.forEach { usuario ->

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
                message = "¿Seguro que quieres eliminar este usuario?",
                onConfirm = {

                    deleting = true

                    scope.launch {

                        eliminarUsuario(selectedUserId!!)

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

                onCreated = {

                    cargarUsuarios()

                    showToast = true
                }
            )
        }
    }
    if (showToast) {

        ToastMessage(

            message = "Usuario creado con éxito",

            onClose = {
                showToast = false
            }
        )
    }
}