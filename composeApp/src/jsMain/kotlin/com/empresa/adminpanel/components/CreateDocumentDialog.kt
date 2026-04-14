package com.empresa.adminpanel.components

import androidx.compose.runtime.*
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

    val scope = rememberCoroutineScope()


    fun subirDocumento() {

        if (selectedUserId.isEmpty() || selectedFile == null)
            return

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
                "http://127.0.0.1:8080/upload?userId=$selectedUserId",
                requestUpload
            ).await()


            if (!uploadResponse.ok) {

                loading = false
                return@launch
            }


            val uploadText = uploadResponse.text().await()

            val json = JSON.parse<dynamic>(uploadText)

            val rutaArchivo = json.url as String


            /* SEGUNDO PASO: registrar documento en BD */

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
                "http://127.0.0.1:8080/documentos",
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

            H3 {

                Text("Subir documento")
            }


            /* USUARIO */

            Select({

                classes(AppStyles.loginInput)

                onChange {

                    selectedUserId =
                        it.target.value
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


            /* TIPO DOCUMENTO */

            Select({

                classes(AppStyles.loginInput)

                onChange {

                    tipo = it.target.value
                }

            }) {

                Option("nomina") {

                    Text("Nómina")
                }

                Option("epis") {

                    Text("EPIs")
                }

                Option("formacion") {

                    Text("Formación")
                }

                Option("reconocimiento") {

                    Text("Reconocimiento")
                }
            }


            /* ARCHIVO */

            Input(InputType.File, attrs = {

                classes(AppStyles.loginInput)

                onInput {

                    val input =
                        it.target as org.w3c.dom.HTMLInputElement

                    val files = input.files

                    if (files != null && files.length > 0) {

                        selectedFile = files.item(0)
                    }
                }
            })


            /* BOTONES */

            Div({

                classes(AppStyles.dialogButtons)

            }) {

                Button({

                    classes(AppStyles.primaryButton)

                    onClick {

                        subirDocumento()
                    }

                }) {

                    if (loading) {

                        Div({

                            classes(AppStyles.loader)

                        }) {}

                    } else {

                        Text("Subir")
                    }
                }


                Button({

                    classes(AppStyles.secondaryButton)

                    onClick {

                        onClose()
                    }

                }) {

                    Text("Cancelar")
                }
            }
        }
    }
}