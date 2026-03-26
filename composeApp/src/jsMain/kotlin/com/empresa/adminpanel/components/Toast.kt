package com.empresa.adminpanel.components

import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import style.AppStyles

@Composable
fun ToastMessage(
    message: String,
    onClose: () -> Unit
) {

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {

        scope.launch {

            delay(4000)

            onClose()
        }
    }

    Div({
        classes(AppStyles.toast)
    }) {

        Text(message)
    }
}