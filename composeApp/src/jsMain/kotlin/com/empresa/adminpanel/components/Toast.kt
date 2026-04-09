package com.empresa.adminpanel.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.animation
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.bottom
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.cursor
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.duration
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.fontWeight
import org.jetbrains.compose.web.css.gap
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.right
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.css.style
import org.jetbrains.compose.web.css.timingFunction
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import style.AppStyles

@Composable
fun Toast(
    message: String,
    type: String = "success",
    durationMs: Int = 3000,
    onClose: () -> Unit
) {

    val background = when (type) {

        "success" -> Color("#16a34a")

        "error" -> Color("#dc2626")

        "warning" -> Color("#f59e0b")

        else -> Color("#334155")
    }


    LaunchedEffect(Unit) {

        kotlinx.coroutines.delay(durationMs.toLong())

        onClose()
    }


    Div({

        style {

            position(Position.Fixed)

            bottom(24.px)

            right(24.px)

            padding(16.px)

            borderRadius(12.px)

            backgroundColor(background)

            color(Color.white)

            fontWeight("600")

            display(DisplayStyle.Flex)

            alignItems(AlignItems.Center)

            gap(12.px)

            animation(AppStyles.fadeIn) {

                duration(0.25.s)

                timingFunction(AnimationTimingFunction.Ease)
            }
        }

    }) {

        Span {

            Text(message)
        }


        Button({

            style {

                backgroundColor(Color.transparent)

                border {

                    style(LineStyle.None)
                }

                color(Color.white)

                fontSize(18.px)

                cursor("pointer")
            }

            onClick {

                onClose()
            }

        }) {

            Text("✕")
        }
    }
}