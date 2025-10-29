package diruptio.aquarium.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import diruptio.aquarium.core.Platform
import diruptio.aquarium.ui.component.*
import diruptio.verticallyspinningfish.Container
import diruptio.verticallyspinningfish.Status
import diruptio.verticallyspinningfish.VerticallySpinningFishApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ContainerView(platform: Platform,
                  coroutineScope: CoroutineScope,
                  api: VerticallySpinningFishApi,
                  container: Container) {
    val online = container.getStatus().isOnline
    Row(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        var loading by remember { mutableStateOf(false) }
        Headline("Container: ${container.getPrettyName(api)}")
        Button(
            onClick = {
                loading = true
                coroutineScope.launch {
                    if (online) {
                        api.stopContainer(container.id)
                    } else {
                        api.startContainer(container.id)
                    }
                }
            },
            enabled = !loading
        ) {
            Text(if (online) "Stop" else "Start")
        }
    }

    Table(
        cellWidths = listOf(0.2f, 0.8f),
        cellModifier = Modifier.padding(8.dp)
    ) {
        row {
            cell(horizontalArrangement = Arrangement.SpaceBetween) {
                Title("ID")
                CopyButton(platform, container.id)
            }
            cell { Text(container.id) }
        }

        row {
            cell(horizontalArrangement = Arrangement.SpaceBetween) {
                Title("Full Name")
                CopyButton(platform, container.name)
            }
            cell { Text(container.name) }
        }

        row {
            var editing by remember { mutableStateOf(false) }
            cell(horizontalArrangement = Arrangement.SpaceBetween) {
                Title("Status")
                if (container.getStatus().isOnline) {
                    IconButton({ editing = true }) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
                    }
                }
            }
            cell {
                if (editing) {
                    StatusSelectMenu(
                        coroutineScope = coroutineScope,
                        open = editing,
                        close = { editing = false },
                        values = listOf(Status.AVAILABLE, Status.UNAVAILABLE),
                        onSelect = { api.setContainerStatus(container.id, it) })
                } else {
                    StatusChip(container.getStatus())
                }
            }
        }

        row {
            cell { Title("Ports") }
            cell {
                Column {
                    container.ports.distinct().forEach { port -> Text(port.toString()) }
                }
            }
        }
    }
}
