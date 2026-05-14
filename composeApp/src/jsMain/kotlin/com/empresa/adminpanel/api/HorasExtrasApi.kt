package com.empresa.adminpanel.api

import com.empresa.adminpanel.ApiClient
import com.empresa.adminpanel.models.HorasExtra
import com.empresa.adminpanel.models.HorasExtrasResumen
import com.empresa.adminpanel.models.RevisarHorasExtra
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.encodeToString
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

    val headers = js("new Headers()")

    headers.append(
        "Authorization",
        ApiClient.authHeader()
    )

    headers.append(
        "Content-Type",
        "application/json"
    )

    console.log(ApiClient.authHeader())

    val response =
        window.fetch(

            "${ApiClient.BASE_URL}/horas-extra/$id",

            jsObject<RequestInit> {

                method = "PUT"

                this.headers = headers

                body =
                    Json.encodeToString(

                        RevisarHorasExtra(

                            estado = estado,

                            comentario = comentario
                        )
                    )
            }
        ).await()


    if (!response.ok) {

        val errorText =
            response.text().await()

        console.error(errorText)

        error(
            "Error actualizando hora extra"
        )
    }
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