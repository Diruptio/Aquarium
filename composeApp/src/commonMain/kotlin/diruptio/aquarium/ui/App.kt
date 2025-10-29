package diruptio.aquarium.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import diruptio.aquarium.core.getPlatform
import diruptio.aquarium.ui.component.Button
import diruptio.verticallyspinningfish.VerticallySpinningFishApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Providers(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = appColorScheme, typography = appTypography) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
            content()
        }
    }
}

@Preview
@Composable
fun App() {
    val platform = remember { getPlatform() }
    val coroutineScope = rememberCoroutineScope()
    var fishes by remember { mutableStateOf(platform.fishManager.fishes) }
    var loadingFishes by remember { mutableStateOf(setOf<String>()) }
    var connectedFishes by remember { mutableStateOf(mapOf<String, VerticallySpinningFishApi>()) }
    var selectedFish by remember { mutableStateOf(0) }
    var fishAddDialogOpen by remember { mutableStateOf(false) }

    DisposableEffect(LocalLifecycleOwner.current) {
        onDispose {
            connectedFishes.values.forEach(VerticallySpinningFishApi::close)
            connectedFishes = emptyMap()
            coroutineScope.cancel()
        }
    }

    Providers {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TabRow(
                    selectedTabIndex = selectedFish,
                    modifier = Modifier.weight(1f)
                ) {
                    fishes.sortedBy { it.name }.forEachIndexed { index, fish ->
                        Tab(
                            selected = selectedFish == index,
                            onClick = { selectedFish = index },
                            modifier = Modifier.pointerHoverIcon(icon = PointerIcon.Hand)
                        ) {
                            Text(fish.name, modifier = Modifier.padding(8.dp))
                        }
                    }
                    Tab(
                        selected = false,
                        onClick = { fishAddDialogOpen = true },
                        modifier = Modifier.pointerHoverIcon(icon = PointerIcon.Hand)
                    ) {
                        Text("Add Fish", modifier = Modifier.padding(8.dp))
                    }
                }
            }

            if (fishes.isEmpty()) {
                Column(
                    modifier = Modifier
                        .safeContentPadding()
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val transition = rememberInfiniteTransition()
                    val position by transition.animateFloat(
                        initialValue = -12f,
                        targetValue = 12f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(500, easing = FastOutLinearInEasing),
                            repeatMode = RepeatMode.Reverse))
                    Icon(
                        imageVector = Icons.Filled.ArrowUpward,
                        contentDescription = null,
                        modifier = Modifier
                            .offset(0.dp, position.dp)
                            .padding(0.dp, 24.dp))
                    Text("You don't have any fishes yet!", style = MaterialTheme.typography.headlineMedium)
                }
            } else {
                val fish = fishes[selectedFish]
                val api = connectedFishes[fish.url]

                if (api == null) {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                loadingFishes += fish.url
                                coroutineScope.launch {
                                    val api = VerticallySpinningFishApi(
                                        platform.httpClientEngine,
                                        fish.url,
                                        fish.secret)
                                    api.open(coroutineScope)
                                    connectedFishes += fish.url to api
                                    loadingFishes -= fish.url
                                }
                            },
                            enabled = fish.url !in loadingFishes
                        ) {
                            Text("Connect to ${fish.name}")
                        }
                    }
                } else {
                    FishView(platform, coroutineScope, api)
                }
            }

            FishAddDialog(
                coroutineScope = coroutineScope,
                fish = null,
                open = fishAddDialogOpen,
                close = { fishAddDialogOpen = false },
                submit = {
                    platform.fishManager.fishes += it
                    fishes += it
                })
        }
    }
}
