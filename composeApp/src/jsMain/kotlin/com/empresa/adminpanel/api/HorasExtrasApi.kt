package com.empresa.adminpanel.api

import com.empresa.adminpanel.ApiClient
import com.empresa.adminpanel.models.HorasExtra
import com.empresa.adminpanel.models.HorasExtrasResumen
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.json.Json
import org.w3c.fetch.RequestInit

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
private inline fun <T> jsObject(builder: T.() -> Unit): T =
    (js("{}") as T).apply(builder)


suspend fun cargarHorasExtrasPendientes(): List<HorasExtra> {

    val token = ApiClient.token
        ?: error("Token no disponible")

    val response = window.fetch(
        "${ApiClient.BASE_URL}/horas-extra/pendientes",
        jsObject<dynamic> {
            method = "GET"
            headers = jsObject<dynamic> {
                this["Authorization"] = "Bearer $token"
            }
        }
    ).await()

    if (!response.ok) {
        error("Error backend: ${response.status}")
    }

    val json = response.text().await()

    return Json.decodeFromString<List<HorasExtra>>(json)
}


suspend fun cargarHorasExtrasPorEstado(
    estado: String
): List<HorasExtra> {

    val response = window.fetch(
        "${ApiClient.BASE_URL}/horas-extra?estado=$estado",
        jsObject<dynamic> {
            method = "GET"
            headers = jsObject<dynamic> {
                this["Authorization"] = ApiClient.authHeader()
            }
        }
    ).await()

    val json = response.text().await()

    return Json.decodeFromString<List<HorasExtra>>(json)
}


suspend fun actualizarHorasExtra(
    id: Int,
    estado: String,
    comentario: String? = null
) {

    window.fetch(
        "${ApiClient.BASE_URL}/horas-extra/$id",
        jsObject<RequestInit> {
            method = "PUT"
            headers = jsObject {
                asDynamic()["Authorization"] = ApiClient.authHeader()
                asDynamic()["Content-Type"] = "application/json"
            }
            body = JSON.stringify(
                jsObject<dynamic> {
                    this.estado = estado
                    this.comentario = comentario
                }
            )
        }
    ).await()

    val queryComentario =
        comentario?.let { "&comentario=$it" } ?: ""

    val response =
        window.fetch(
            "${ApiClient.BASE_URL}/horas-extra/$id?estado=$estado$queryComentario",
            jsObject {
                method = "PUT"
                headers = jsObject {
                    this["Authorization"] = ApiClient.authHeader()
                }
            }
        ).await()

    if (!response.ok)
        error("Error actualizando hora extra")
}

suspend fun cargarHorasExtrasPorEstadoYUsuario(
    estado: String,
    userId: Int
): List<HorasExtra> {

    val response = window.fetch(
        "${ApiClient.BASE_URL}/horas-extra?estado=$estado&userId=$userId",
        jsObject<RequestInit> {
            method = "GET"
            headers = jsObject {
                asDynamic()["Authorization"] = ApiClient.authHeader()
            }
        }
    ).await()

    val json = response.text().await()

    return Json.decodeFromString(json)
}

suspend fun cargarResumenHorasExtras(): HorasExtrasResumen {

    val token =
        ApiClient.token ?: error("Token no disponible")

    val response =
        window.fetch(
            "${ApiClient.BASE_URL}/horas-extra/resumen",
            jsObject<dynamic> {

                method = "GET"

                headers =
                    jsObject<dynamic> {

                        this["Authorization"] =
                            "Bearer $token"
                    }
            }
        ).await()

    if (!response.ok)
        error("Error cargando resumen")

    val json =
        response.text().await()

    return Json.decodeFromString(json)
}