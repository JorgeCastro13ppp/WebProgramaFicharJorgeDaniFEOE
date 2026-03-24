package com.empresa.adminpanel.screens

import androidx.compose.runtime.*
import com.empresa.adminpanel.models.Usuario
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.dom.*
import org.w3c.fetch.Headers

@Composable
fun UsuariosScreen() {

    var usuarios by remember {
        mutableStateOf<List<Usuario>>(emptyList())
    }

    LaunchedEffect(Unit) {

        val token = window.localStorage.getItem("token")
            ?: return@LaunchedEffect

        println("TOKEN = $token")

        val headers = Headers()

        headers.append(
            "Authorization",
            "Bearer $token"
        )

        val requestInit = js("{}")

        requestInit.method = "GET"
        requestInit.headers = headers

        val response = window.fetch(
            "http://127.0.0.1:8080/admin/usuarios",
            requestInit
        ).await()

        println("STATUS = ${response.status}")

        if (response.ok) {

            val text = response.text().await()

            usuarios =
                Json.decodeFromString<List<Usuario>>(text)
        }
    }

    Div {

        H2 { Text("Usuarios registrados") }

        Table {

            Tr {
                Th { Text("ID") }
                Th { Text("Usuario") }
                Th { Text("Rol") }
                Th { Text("Acción") }
            }

            usuarios.forEach { usuario ->

                Tr {

                    Td { Text("${usuario.id}") }

                    Td { Text(usuario.username) }

                    Td { Text(usuario.role) }

                    Td {

                        Button(attrs = {

                            onClick {
                                println("Eliminar ${usuario.id}")
                            }

                        }) {

                            Text("Eliminar")
                        }
                    }
                }
            }
        }
    }
}