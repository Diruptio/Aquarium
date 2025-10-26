package diruptio.aquarium.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import diruptio.aquarium.core.Platform
import diruptio.aquarium.ui.component.CopyButton
import diruptio.aquarium.ui.component.Headline
import diruptio.aquarium.ui.component.Table
import diruptio.aquarium.ui.component.Title
import diruptio.verticallyspinningfish.Group
import diruptio.verticallyspinningfish.Status
import diruptio.verticallyspinningfish.VerticallySpinningFishApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

@Composable
fun GroupView(platform: Platform,
                  coroutineScope: CoroutineScope,
                  api: VerticallySpinningFishApi,
                  group: Group) {
    val containers = api.getContainersByGroup(group.name)
    Row(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        var loading by remember { mutableStateOf(false) }
        Headline("Group: ${group.prettyName}")
        Button(
            onClick = {
                loading = true
                coroutineScope.launch {
                    containers.map { container ->
                        coroutineScope.launch {
                            api.stopContainer(container.id)
                        }
                    }.joinAll()
                    loading = false
                }
            },
            enabled = !loading
        ) {
            Text("Stop all containers")
        }
    }

    Table(
        cellWidths = listOf(0.4f, 0.6f),
        cellModifier = Modifier.padding(8.dp)
    ) {
        row {
            cell(horizontalArrangement = Arrangement.SpaceBetween) {
                Title("Full Name")
                CopyButton(platform, group.name)
            }
            cell { Text(group.name) }
        }

        row {
            cell(horizontalArrangement = Arrangement.SpaceBetween) {
                Title("Minimum Instance Count")
            }
            cell { Text(group.minCount.toString()) }
        }

        row {
            cell(horizontalArrangement = Arrangement.SpaceBetween) {
                Title("Instance Count")
            }
            cell { Text(containers.size.toString()) }
        }

        row {
            cell(horizontalArrangement = Arrangement.SpaceBetween) {
                Title("Available Instance Count")
            }
            cell { Text(containers.count { it.getStatus() == Status.AVAILABLE }.toString()) }
        }

        row {
            cell(horizontalArrangement = Arrangement.SpaceBetween) {
                Title("Tags")
            }
            cell {
                Column {
                    group.tags.forEach { port -> Text(port) }
                }
            }
        }
    }
}
