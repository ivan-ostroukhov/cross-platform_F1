package com.example.apex

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import f1archive.shared.generated.resources.Res
import f1archive.shared.generated.resources.app_title
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString

fun main() = application {
    val windowTitle = runBlocking {
        getString(Res.string.app_title)
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = windowTitle
    ) {
        App()
    }
}