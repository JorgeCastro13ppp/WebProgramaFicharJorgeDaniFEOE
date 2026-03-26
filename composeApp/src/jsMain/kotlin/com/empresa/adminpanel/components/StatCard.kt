package com.empresa.adminpanel.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import style.AppStyles

@Composable
fun StatCard(
    title: String,
    value: String,
    accent: StatAccent
) {

    val accentStyle = when (accent) {
        StatAccent.BLUE -> AppStyles.statAccentBlue
        StatAccent.GREEN -> AppStyles.statAccentGreen
        StatAccent.ORANGE -> AppStyles.statAccentOrange
        StatAccent.RED -> AppStyles.statAccentRed
    }

    Div({
        classes(AppStyles.statCard)
    }) {

        Div({
            classes(accentStyle)
        }) {}

        Div({
            classes(AppStyles.statTitle)
        }) {
            Text(title)
        }

        Div({
            classes(AppStyles.statValue)
        }) {
            Text(value)
        }
    }
}