package com.empresa.adminpanel.screens

import androidx.compose.runtime.*
import com.empresa.adminpanel.api.*
import com.empresa.adminpanel.components.*
import com.empresa.adminpanel.models.HorasExtra
import com.empresa.adminpanel.models.HorasExtrasResumen
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.gap
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.textAlign
import org.jetbrains.compose.web.dom.*
import style.AppStyles
import kotlin.js.Date

@Composable
fun HorasExtrasScreen() {

    var extras by remember { mutableStateOf<List<HorasExtra>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    var filtroUserId by remember { mutableStateOf<Int?>(null) }
    var selectedEstado by remember { mutableStateOf("pendiente") }

    var selectedId by remember { mutableStateOf<Int?>(null) }
    var accion by remember { mutableStateOf<String?>(null) }

    var resumen by remember {
        mutableStateOf<HorasExtrasResumen?>(null)
    }

    var comentario by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()


    fun recargar() {

        scope.launch {

            loading = true

            resumen = cargarResumenHorasExtras()

            extras =
                if (selectedEstado == "pendiente")
                    cargarHorasExtrasPendientes()
                else
                    filtroUserId?.let {
                        cargarHorasExtrasPorEstadoYUsuario(
                            selectedEstado,
                            it
                        )
                    } ?: cargarHorasExtrasPorEstado(selectedEstado)

            loading = false
        }
    }


    LaunchedEffect(selectedEstado) {
        recargar()
    }


    ScreenHeader(
        title = "Horas extra",
        onRefresh = { recargar() }
    )


    /*
    ========================
    STATS
    ========================
    */

    Div({

        classes(AppStyles.statsGrid)

    }) {

        StatCard(
            "Pendientes",
            (resumen?.pendientes ?: 0).toString(),
            StatAccent.YELLOW
        )

        StatCard(
            "Aprobadas",
            (resumen?.aprobadas ?: 0).toString(),
            StatAccent.GREEN
        )

        StatCard(
            "Rechazadas",
            (resumen?.rechazadas ?: 0).toString(),
            StatAccent.RED
        )

        StatCard(
            "Horas totales",
            ((resumen?.totalMinutos ?: 0) / 60).toString(),
            StatAccent.BLUE
        )
    }


    /*
    ========================
    FILTROS + TABS
    ========================
    */

    Div({

        classes(AppStyles.filtersRow)

    }) {

        listOf(
            "pendiente",
            "aprobado",
            "rechazado"
        ).forEach { estado ->

            Button({

                classes(
                    if (selectedEstado == estado)
                        AppStyles.filterActive
                    else
                        AppStyles.filterButton
                )

                onClick {

                    selectedEstado = estado
                    filtroUserId = null
                }

            }) {

                Text(
                    estado.replaceFirstChar { it.uppercase() }
                )
            }
        }


        if (selectedEstado != "pendiente") {

            Input(InputType.Number, attrs = {

                classes(AppStyles.input)

                placeholder("Filtrar por userId")

                onInput {

                    val value = it.value?.toString()

                    filtroUserId =
                        if (value.isNullOrBlank())
                            null
                        else
                            value.toIntOrNull()
                }

            })

            Button({

                classes(AppStyles.primaryButton)

                onClick { recargar() }

            }) {

                Text("Aplicar filtro")
            }
        }
    }


    /*
    ========================
    TABLA
    ========================
    */

    if (loading) {

        LoadingSpinner()
        return
    }


    if (extras.isEmpty()) {

        P({

            classes(AppStyles.emptyState)

        }) {

            Text("No hay resultados")
        }

        return
    }

    Div({

        classes(AppStyles.card)

    }) {

        Table({

            classes(AppStyles.tableFlat)

        }) {

            Tr {

                Th { Text("Usuario") }

                Th { Text("Nombre") }

                Th { Text("Fecha") }

                Th({
                    style { textAlign("center") }
                }) {
                    Text("Minutos")
                }

                if (selectedEstado != "pendiente") {

                    Th { Text("Aprobado por") }

                    Th { Text("Fecha revisión") }

                    Th { Text("Comentario") }
                }

                if (selectedEstado == "pendiente") {

                    Th({
                        style { textAlign("center") }
                    }) {
                        Text("Acciones")
                    }
                }
            }


            extras.forEach { extra ->

                Tr {

                    Td { Text(extra.userId.toString()) }

                    Td { Text(extra.username) }

                    Td { Text(extra.fecha) }

                    Td({
                        style { textAlign("center") }
                    }) {
                        Text("${extra.minutosExtra} min")
                    }


                    if (selectedEstado != "pendiente") {

                        Td {
                            Text(extra.aprobadoPor?.toString() ?: "-")
                        }

                        Td {

                            Text(
                                extra.fechaRevision
                                    ?.let { Date(it).toLocaleString() }
                                    ?: "-"
                            )
                        }

                        Td {

                            Span({

                                title(
                                    extra.comentario
                                        ?: "Sin comentario"
                                )

                            }) {

                                Text(extra.comentario ?: "-")
                            }
                        }
                    }


                    if (selectedEstado == "pendiente") {

                        Td({

                            style {
                                textAlign("center")
                            }

                        }) {

                            Div({

                                style {
                                    display(DisplayStyle.Flex)
                                    justifyContent(JustifyContent.Center)
                                    gap(8.px)
                                }

                            }) {

                                Button({

                                    classes(AppStyles.successButton)

                                    onClick {

                                        selectedId = extra.id
                                        accion = "aprobado"
                                        comentario = ""
                                    }

                                }) {
                                    Img("/icons/check.svg"){
                                        classes(AppStyles.deleteIcon)
                                    }
                                    Text("Aprobar")
                                }


                                Button({

                                    classes(AppStyles.dangerButton)

                                    onClick {

                                        selectedId = extra.id
                                        accion = "rechazado"
                                        comentario = ""
                                    }

                                }) {
                                    Img("/icons/cross.svg"){
                                        classes(AppStyles.deleteIcon)
                                    }
                                    Text("Rechazar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}