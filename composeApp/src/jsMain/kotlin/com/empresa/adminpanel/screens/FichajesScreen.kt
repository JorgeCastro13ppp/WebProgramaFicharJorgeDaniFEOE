package com.empresa.adminpanel.screens

import androidx.compose.runtime.*
import com.empresa.adminpanel.components.ConfirmDialog
import com.empresa.adminpanel.models.Fichaje
import com.empresa.adminpanel.models.Usuario
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
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

    val scope = rememberCoroutineScope()


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

                usuarios =
                    Json.decodeFromString(text)
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


    LaunchedEffect(Unit) {

        cargarUsuarios()

        cargarFichajes()
    }


    LaunchedEffect(selectedUserId) {

        cargarFichajes()
    }


    /* HEADER */

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

            /* IZQUIERDA: título + botón */

            Div({

                style {

                    display(DisplayStyle.Flex)

                    alignItems(AlignItems.Center)

                    gap(16.px)
                }

            }) {

                H2({

                    classes(AppStyles.title)

                }) {

                    Text("Fichajes")
                }


                Button({

                    classes(AppStyles.secondaryButton)

                    onClick {

                        cargarFichajes()
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


            /* DERECHA: filtro usuarios */

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

                        fichajes.forEach { fichaje ->

                            val fecha =
                                Date(fichaje.fechaHora)
                                    .toLocaleDateString()

                            val hora =
                                Date(fichaje.fechaHora)
                                    .toLocaleTimeString()

                            val usuario = fichaje.username

                            Tr {

                                Td { Text("${fichaje.id}") }

                                Td { Text(usuario) }

                                Td { Text(fecha) }

                                Td { Text(hora) }

                                Td {

                                    val badgeStyle =
                                        if (fichaje.tipo == "entrada")
                                            AppStyles.badgeEntrada
                                        else
                                            AppStyles.badgeSalida


                                    Span({

                                        classes(
                                            AppStyles.badgeFichar,
                                            badgeStyle
                                        )

                                    }) {

                                        Text(fichaje.tipo)
                                    }
                                }


                                Td {

                                    Button({

                                        classes(AppStyles.deleteButton)

                                        onClick {

                                            selectedFichajeId =
                                                fichaje.id

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
}