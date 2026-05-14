package com.empresa.adminpanel.api

import com.empresa.adminpanel.ApiClient
import com.empresa.adminpanel.models.CorregirJornadaRequest
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.fetch.RequestInit
import kotlin.js.json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


suspend fun actualizarJornada(
    jornadaId: Int,
    nuevaEntradaReal: Long,
    nuevaSalidaReal: Long,
    comentario: String? = null
) {

    val request =

        CorregirJornadaRequest(

            jornadaId = jornadaId,

            nuevaEntradaReal = nuevaEntradaReal,

            nuevaSalidaReal = nuevaSalidaReal,

            comentario = comentario
        )


    val response =

        window.fetch(

            "${ApiClient.BASE_URL}/jornadas/corregir",

            object : RequestInit {

                override var method: String? =
                    "PUT"

                override var headers: dynamic =
                    json(
                        "Authorization" to ApiClient.authHeader(),
                        "Content-Type" to "application/json"
                    )

                override var body: dynamic =
                    Json.encodeToString(request)
            }
        ).await()


    if (!response.ok) {

        val error =
            response.text().await()

        error(error)
    }
}