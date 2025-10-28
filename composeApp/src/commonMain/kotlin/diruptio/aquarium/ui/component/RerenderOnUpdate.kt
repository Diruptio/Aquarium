package diruptio.aquarium.ui.component

import androidx.compose.runtime.*
import diruptio.verticallyspinningfish.Container
import diruptio.verticallyspinningfish.Group
import diruptio.verticallyspinningfish.VerticallySpinningFishApi

@Composable
fun <T> RerenderOnContainerUpdate(api: VerticallySpinningFishApi, content: @Composable () -> T): T {
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

@Composable
fun <T> RerenderOnGroupUpdate(api: VerticallySpinningFishApi, content: @Composable () -> T): T {
    var forceRerenderKey by remember { mutableStateOf(0) }

    DisposableEffect(0) {
        val listener = { _: Group -> forceRerenderKey += 1 }
        api.groupUpdateListeners += listener
        onDispose {
            api.groupUpdateListeners -= listener
        }
    }

    return key(forceRerenderKey) {
        content()
    }
}