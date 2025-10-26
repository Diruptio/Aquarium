package diruptio.aquarium.ui.component

import androidx.compose.runtime.*
import diruptio.verticallyspinningfish.Container
import diruptio.verticallyspinningfish.VerticallySpinningFishApi

@Composable
fun <T> RerenderOnUpdate(api: VerticallySpinningFishApi, content: @Composable () -> T): T {
    var forceRerenderKey by remember { mutableStateOf(0) }

    DisposableEffect(0) {
        val listener = { _: Container -> forceRerenderKey += 1 }
        api.containerAddListeners += listener
        api.containerRemoveListeners += listener
        api.containerStatusListeners += listener
        onDispose {
            api.containerAddListeners -= listener
            api.containerRemoveListeners -= listener
            api.containerStatusListeners -= listener
        }
    }

    return key(forceRerenderKey) {
        content()
    }
}
