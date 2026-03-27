package com.empresa.adminpanel.screens

import androidx.compose.runtime.*
import com.empresa.adminpanel.components.ConfirmDialog
import com.empresa.adminpanel.models.Documento
import com.empresa.adminpanel.models.Usuario
import kotlinx.browser.window
import kotlinx.coroutines.launch
import kotlinx.coroutines.await
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.gap
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.css.marginBottom
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*
import org.w3c.fetch.Headers
import style.AppStyles


@Composable
fun DocumentosScreen() {

    var usuarios by remember {
        mutableStateOf<List<Usuario>>(emptyList())
    }

    var selectedUserId by remember {
        mutableStateOf("todos")
    }

    var selectedTipo by remember {
        mutableStateOf("todos")
    }

    var documentos by remember {
        mutableStateOf<List<Documento>>(emptyList())
    }

    var loading by remember {
        mutableStateOf(true)
    }

    var selectedDocumentoId by remember {
        mutableStateOf<Int?>(null)
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    fun cargarDocumentos() {

        scope.launch {

            loading = true

            val token = window.localStorage.getItem("token")

            if (token == null) {

                loading = false
                return@launch
            }

            val headers = Headers()

            headers.append("Authorization", "Bearer $token")

            var url = "http://127.0.0.1:8080/admin/documentos?"

            if (selectedUserId != "todos") {

                url += "userId=$selectedUserId&"
            }

            if (selectedTipo != "todos") {

                url += "tipo=$selectedTipo"
            }

            val requestInit = js("{}")

            requestInit.method = "GET"
            requestInit.headers = headers

            val response = window.fetch(
                url,
                requestInit
            ).await()

            if (response.ok) {

                val text = response.text().await()

                documentos =
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

    fun eliminarDocumento(id: Int) {

        scope.launch {

            val token = window.localStorage.getItem("token")
                ?: return@launch

            val headers = Headers()

            headers.append("Authorization", "Bearer $token")

            val requestInit = js("{}")
            requestInit.method = "DELETE"
            requestInit.headers = headers

            window.fetch(
                "http://127.0.0.1:8080/admin/documentos/$id",
                requestInit
            ).await()

            cargarDocumentos()
        }
    }

    LaunchedEffect(Unit) {
        cargarUsuarios()

        cargarDocumentos()
    }

    Div {

        /* HEADER */

        Div({

            style {

                display(DisplayStyle.Flex)

                justifyContent(JustifyContent.SpaceBetween)

                alignItems(AlignItems.Center)

                marginBottom(24.px)
            }

        }) {

            /* IZQUIERDA: título + botón recargar */

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

                    Text("Documentos")
                }


                Button({

                    classes(AppStyles.secondaryButton)

                    onClick {

                        cargarDocumentos()
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


            /* DERECHA: filtros */

            Div({

                style {

                    display(DisplayStyle.Flex)

                    gap(12.px)
                }

            }) {

                /* FILTRO USUARIO */

                Select({
                    classes(AppStyles.filterSelect)
                    onChange {

                        selectedUserId = it.target.value

                        cargarDocumentos()
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


                /* FILTRO TIPO DOCUMENTO */

                Select({

                    classes(AppStyles.filterSelect)
                    onChange {

                        selectedTipo = it.target.value

                        cargarDocumentos()
                    }

                }) {

                    Option("todos") {

                        Text("Todos los tipos")
                    }

                    Option("nomina") {

                        Text("Nómina")
                    }

                    Option("reconocimiento") {

                        Text("Reconocimiento")
                    }

                    Option("formacion") {

                        Text("Formación")
                    }

                    Option("epis") {

                        Text("EPIs")
                    }
                }
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

                            Th { Text("Nombre") }

                            Th { Text("Tipo") }

                            Th { Text("Abrir") }

                            Th { Text("Acción") }
                        }
                    }


                    Tbody {

                        documentos.forEach { doc ->

                            val badgeStyle = when (doc.tipo.lowercase()) {

                                "nomina" -> AppStyles.badgeNomina

                                "reconocimiento" -> AppStyles.badgeReconocimiento

                                "formacion" -> AppStyles.badgeFormacion

                                "epis" -> AppStyles.badgeEpis

                                else -> AppStyles.badgePdf
                            }


                            Tr {

                                Td {

                                    Text("${doc.id}")
                                }


                                Td {

                                    Text(doc.username)
                                }


                                Td {

                                    Text(doc.nombre)
                                }


                                Td {

                                    Span({

                                        classes(
                                            AppStyles.badgeDocumento,
                                            badgeStyle
                                        )

                                    }) {

                                        Text(
                                            doc.tipo.replaceFirstChar {
                                                it.uppercase()
                                            }
                                        )
                                    }
                                }


                                /* BOTÓN ABRIR */

                                Td({

                                    classes(AppStyles.actionCell)

                                }) {

                                    Button({

                                        classes(AppStyles.openButton)

                                        onClick {

                                            window.open(
                                                "http://127.0.0.1:8080${doc.url}",
                                                "_blank"
                                            )
                                        }

                                    }) {

                                        Text("Abrir")
                                    }
                                }


                                /* BOTÓN ELIMINAR */

                                Td({

                                    classes(AppStyles.actionCell)

                                }) {

                                    Button({

                                        classes(AppStyles.deleteButton)

                                        onClick {

                                            selectedDocumentoId = doc.id

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


        /* CONFIRM DIALOG */

        if (showDialog && selectedDocumentoId != null) {

            ConfirmDialog(

                message = "¿Eliminar documento?",

                confirmText = "Eliminar",

                confirmClass = AppStyles.deleteButton,

                onConfirm = {

                    eliminarDocumento(selectedDocumentoId!!)

                    showDialog = false
                },

                onCancel = {

                    showDialog = false
                }
            )
        }
    }
}