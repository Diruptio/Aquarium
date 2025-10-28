package diruptio.aquarium.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import diruptio.aquarium.core.Platform
import diruptio.aquarium.ui.component.Headline
import diruptio.aquarium.ui.component.RerenderOnContainerUpdate
import diruptio.aquarium.ui.component.RerenderOnGroupUpdate
import diruptio.verticallyspinningfish.Container
import diruptio.verticallyspinningfish.Group
import diruptio.verticallyspinningfish.VerticallySpinningFishApi
import kotlinx.coroutines.CoroutineScope

@Composable
fun FishView(platform: Platform, coroutineScope: CoroutineScope, api: VerticallySpinningFishApi) {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        var selectedTab by remember { mutableStateOf(0) }
        var selectedGroup by remember { mutableStateOf<Group?>(null) }
        var selectedContainer by remember { mutableStateOf<Container?>(null) }

        DisposableEffect(selectedGroup) {
            val groupUpdateListener = { group: Group ->
                if (group.name == selectedGroup?.name) {
                    selectedGroup = group
                }
            }
            api.groupUpdateListeners += groupUpdateListener
            onDispose {
                api.groupUpdateListeners -= groupUpdateListener
            }
        }

        Surface(
            modifier = Modifier.selectableGroup(),
            contentColor = TabRowDefaults.primaryContentColor
        ) {
            Column(Modifier.fillMaxHeight().width(IntrinsicSize.Max)) {
                listOf("Groups", "Containers").forEachIndexed { index, title ->
                    val selected = selectedTab == index
                    val background by animateColorAsState(if (selected) {
                        TabRowDefaults.primaryContentColor.copy(alpha = 0.3f)
                    } else {
                        Color.Transparent
                    })

                    Column(
                        modifier = Modifier
                            .background(background)
                            .selectable(
                                selected = selected,
                                onClick = {
                                    selectedTab = index
                                    selectedGroup = null
                                    selectedContainer = null
                                },
                                role = Role.Tab,
                                interactionSource = null,
                                indication = ripple(bounded = true, color = LocalContentColor.current))
                            .pointerHoverIcon(PointerIcon.Hand)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = title, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (selectedGroup != null) {
                val groupState = remember(selectedGroup) { mutableStateOf(selectedGroup!!) }
                RerenderOnContainerUpdate(api) {
                    GroupView(platform, coroutineScope, api, groupState)
                }
            } else if (selectedContainer != null) {
                var forceRerenderKey by remember { mutableStateOf(0) }

                DisposableEffect(selectedContainer) {
                    val removeListener = { container: Container ->
                        if (container == selectedContainer) {
                            selectedContainer = null
                        }
                    }
                    val statusListener = { container: Container ->
                        if (container == selectedContainer) {
                            forceRerenderKey++
                        }
                    }
                    api.containerRemoveListeners += removeListener
                    api.containerStatusListeners += statusListener
                    onDispose {
                        api.containerRemoveListeners -= removeListener
                        api.containerStatusListeners -= statusListener
                    }
                }

                key(forceRerenderKey) {
                    ContainerView(platform, coroutineScope, api, selectedContainer!!)
                }
            } else {
                when (selectedTab) {
                    0 -> {
                        RerenderOnGroupUpdate(api) {
                            RerenderOnContainerUpdate(api) {
                                Headline("Groups")
                                GroupsTable(api) { selectedGroup = it }
                            }
                        }
                    }

                    1 -> {
                        RerenderOnContainerUpdate(api) {
                            Headline("Containers")
                            ContainersTable(api) { selectedContainer = it }
                        }
                    }
                }
            }
        }
    }
}
