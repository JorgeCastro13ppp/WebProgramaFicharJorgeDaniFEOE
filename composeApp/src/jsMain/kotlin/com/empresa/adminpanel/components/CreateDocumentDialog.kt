package com.empresa.adminpanel.components

import androidx.compose.runtime.*
import com.empresa.adminpanel.ApiClient
import com.empresa.adminpanel.models.Usuario
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.*
import org.w3c.fetch.Headers
import org.w3c.files.File
import org.w3c.xhr.FormData
import style.AppStyles

import org.jetbrains.compose.web.attributes.disabled

@Composable
fun CreateDocumentoDialog(

    usuarios: List<Usuario>,

    onClose: () -> Unit,

    onUploaded: () -> Unit

) {

    var selectedUserId by remember { mutableStateOf("") }

    var tipo by remember { mutableStateOf("nomina") }

    var selectedFile by remember { mutableStateOf<File?>(null) }

    var loading by remember { mutableStateOf(false) }


    /* VALIDACIÓN VISUAL */

    var userError by remember { mutableStateOf(false) }

    var fileError by remember { mutableStateOf(false) }


    val scope = rememberCoroutineScope()


    fun subirDocumento() {

        userError = selectedUserId.isEmpty()
        fileError = selectedFile == null

        if (userError || fileError) return

        scope.launch {

            loading = true

            val token =
                window.localStorage.getItem("token")
                    ?: return@launch


            val formData = FormData()

            formData.append("file", selectedFile!!)
            formData.append("userId", selectedUserId)


            val headers = Headers()

            headers.append("Authorization", "Bearer $token")


            val requestUpload = js("{}")

            requestUpload.method = "POST"
            requestUpload.headers = headers
            requestUpload.body = formData


            val uploadResponse = window.fetch(
                "${ApiClient.BASE_URL}/upload?userId=$selectedUserId",
                requestUpload
            ).await()


            if (!uploadResponse.ok) {

                loading = false
                return@launch
            }


            val uploadText = uploadResponse.text().await()

            val json = JSON.parse<dynamic>(uploadText)

            val rutaArchivo = json.url as String


            val documentObject = js("{}")

            documentObject.userId = selectedUserId.toInt()
            documentObject.nombre = selectedFile!!.name
            documentObject.tipo = tipo
            documentObject.url = rutaArchivo

            val documentBody = JSON.stringify(documentObject)


            val headersDocument = Headers()

            headersDocument.append("Authorization", "Bearer $token")
            headersDocument.append("Content-Type", "application/json")


            val requestDocument = js("{}")

            requestDocument.method = "POST"
            requestDocument.headers = headersDocument
            requestDocument.body = documentBody


            val saveResponse = window.fetch(
                "${ApiClient.BASE_URL}/documentos",
                requestDocument
            ).await()


            loading = false


            if (saveResponse.ok) {

                onUploaded()
                onClose()
            }
        }
    }


    Div({

        classes(AppStyles.dialogOverlay)

    }) {

        Div({

            classes(AppStyles.dialogBox)

        }) {

            H3({

                classes(AppStyles.dialogTitle)

            }) {

                Text("Subir documento")
            }


            Div({

                classes(AppStyles.dialogForm)

            }) {

                /* USUARIO */

                Select({

                    classes(AppStyles.dialogInput)

                    if (userError) {

                        classes(AppStyles.dialogInputError)
                    }

                    onChange {

                        selectedUserId = it.target.value

                        userError = false
                    }

                }) {

                    Option("") {

                        Text("Seleccionar usuario")
                    }

                    usuarios.forEach {

                        Option(it.id.toString()) {

                            Text(it.username)
                        }
                    }
                }

                if (userError) {

                    Span({

                        classes(AppStyles.inputErrorText)

                    }) {

                        Text("Selecciona un usuario")
                    }
                }


                /* TIPO DOCUMENTO */

                Select({

                    classes(AppStyles.dialogInput)

                    onChange {

                        tipo = it.target.value
                    }

                }) {

                    Option("nomina") { Text("Nómina") }

                    Option("epis") { Text("EPIs") }

                    Option("formacion") { Text("Formación") }

                    Option("reconocimiento") { Text("Reconocimiento") }
                }


                /* ARCHIVO */

                Input(InputType.File, attrs = {

                    classes(AppStyles.dialogInput)

                    if (userError) {

                        classes(AppStyles.dialogInputError)
                    }

                    onInput {

                        val input =
                            it.target as org.w3c.dom.HTMLInputElement

                        val files = input.files

                        if (files != null && files.length > 0) {

                            selectedFile = files.item(0)

                            fileError = false
                        }
                    }
                })

                if (fileError) {

                    Span({

                        classes(AppStyles.inputErrorText)

                    }) {

                        Text("Selecciona un archivo")
                    }
                }
            }


            Div({

                classes(AppStyles.dialogActions)

            }) {

                Button({

                    classes(AppStyles.secondaryButton)

                    onClick {

                        onClose()
                    }

                }) {

                    Text("Cancelar")
                }


                Button(attrs = {

                    classes(AppStyles.primaryButton)

                    if (
                        selectedUserId.isEmpty()
                        || selectedFile == null
                        || loading
                    ) {

                        disabled()

                        classes(AppStyles.primaryButtonDisabled)
                    }

                    onClick {

                        subirDocumento()
                    }

                }) {

                    if (loading) {

                        Div({

                            classes(AppStyles.loaderSmall)

                        }) {}

                    } else {

                        Text("Subir")
                    }
                }
            }
        }
    }
}