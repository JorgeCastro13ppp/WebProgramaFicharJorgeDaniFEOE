package com.empresa.adminpanel.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Div
import style.AppStyles

@Composable
fun LoadingSpinner() {

    Div({
        classes(AppStyles.loaderContainer)
    }) {

        Div({
            classes(AppStyles.loader)
        }) {}
    }
}