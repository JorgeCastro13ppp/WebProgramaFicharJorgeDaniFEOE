package com.empresa.adminpanel.screens

import androidx.compose.runtime.*
import com.empresa.adminpanel.models.Documento
import kotlinx.browser.window
import kotlinx.coroutines.launch
import kotlinx.coroutines.await
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.dom.*
import org.w3c.fetch.Headers


@Composable
fun DocumentosScreen() {

    var documentos by remember {
        mutableStateOf<List<Documento>>(emptyList())
    }

    val scope = rememberCoroutineScope()

    fun cargarDocumentos() {

        scope.launch {

            val token = window.localStorage.getItem("token")
                ?: return@launch

            val headers = Headers()

            headers.append("Authorization", "Bearer $token")

            val requestInit = js("{}")
            requestInit.method = "GET"
            requestInit.headers = headers

            val response = window.fetch(
                "http://127.0.0.1:8080/documentos",
                requestInit
            ).await()

            if (response.ok) {

                val text = response.text().await()

                documentos =
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
        cargarDocumentos()
    }

    Div {

        H2 { Text("Documentos") }

        documentos.forEach { doc ->

            Div {

                Text(
                    "ID ${doc.id} | Usuario ${doc.userId} | ${doc.nombre} | ${doc.tipo}"
                )

                Button(attrs = {
                    onClick {
                        window.open(
                            "http://127.0.0.1:8080${doc.url}",
                            "_blank"
                        )
                    }
                }) { Text("Abrir") }

                Button(attrs = {
                    onClick {
                        eliminarDocumento(doc.id)
                    }
                }) { Text("Eliminar") }

                Br()
            }
        }
    }
}