package com.empresa.adminpanel

import androidx.compose.runtime.*
import com.empresa.adminpanel.components.AdminLayout
import com.empresa.adminpanel.components.Toast
import com.empresa.adminpanel.screens.LoginScreen
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable
import style.AppStyles

fun main() {

    renderComposable(rootElementId = "root") {

        Style(AppStyles)

        var loggedIn by remember { mutableStateOf(false) }

        var toastMessage by remember {
            mutableStateOf<String?>(null)
        }

        var toastType by remember {
            mutableStateOf("success")
        }


        /* FUNCIÓN GLOBAL TOAST */

        fun showToast(
            message: String,
            type: String
        ) {

            toastMessage = message
            toastType = type
        }


        /* LOGIN / APP */

        if (!loggedIn) {

            LoginScreen(

                onLoginSuccess = {

                    loggedIn = true
                },

                showToast = { message, type ->

                    showToast(message, type)
                }
            )

        } else {

            AdminLayout(

                onLogout = {

                    loggedIn = false
                },

                showToast = { message, type ->

                    showToast(message, type)
                }
            )
        }


        /* TOAST GLOBAL */

        toastMessage?.let {

            Toast(

                message = it,

                type = toastType,

                onClose = {

                    toastMessage = null
                }
            )
        }
    }
}