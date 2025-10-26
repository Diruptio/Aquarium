package diruptio.aquarium

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import diruptio.aquarium.ui.App

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Aquarium (Vertically Spinning Fish)") {
        App()
    }
}
