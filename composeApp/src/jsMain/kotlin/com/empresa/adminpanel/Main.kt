package com.empresa.adminpanel

import androidx.compose.runtime.*
import com.empresa.adminpanel.components.AdminLayout
import com.empresa.adminpanel.screens.*
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable
import style.AppStyles

fun main() {

    renderComposable(rootElementId = "root") {

        Style(AppStyles)

        var loggedIn by remember { mutableStateOf(false) }

        if (!loggedIn) {

            LoginScreen {
                loggedIn = true
            }

        } else {

            AdminLayout {
                loggedIn = false
            }
        }
    }
}

