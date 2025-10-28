package diruptio.aquarium.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import diruptio.aquarium.core.Platform
import diruptio.aquarium.ui.component.CopyButton
import diruptio.aquarium.ui.component.Headline
import diruptio.aquarium.ui.component.Table
import diruptio.aquarium.ui.component.Title
import diruptio.verticallyspinningfish.Group
import diruptio.verticallyspinningfish.GroupUpdateRequest
import diruptio.verticallyspinningfish.Status
import diruptio.verticallyspinningfish.VerticallySpinningFishApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

@Composable
fun GroupView(
    platform: Platform,
    coroutineScope: CoroutineScope,
    api: VerticallySpinningFishApi,
    groupState: MutableState<Group>
) {
    val group by groupState
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
            var editing by remember { mutableStateOf(false) }
            cell(horizontalArrangement = Arrangement.SpaceBetween) {
                Title("Minimum Instance Count")
                if (!editing) {
                    IconButton({ editing = true }, Modifier.pointerHoverIcon(PointerIcon.Hand)) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
                    }
                }
            }
            cell {
                if (editing) {
                    var minCount by remember { mutableStateOf(group.minCount.toString()) }
                    OutlinedTextField(
                        value = minCount,
                        onValueChange = { value -> if (value.all { it.isDigit() }) minCount = value },
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        trailingIcon = {
                            Row {
                                IconButton(onClick = {
                                    coroutineScope.launch {
                                        api.updateGroup(GroupUpdateRequest(name = group.name, minCount = minCount.toIntOrNull() ?: 0))
                                        editing = false
                                    }
                                }) {
                                    Icon(Icons.Filled.Done, contentDescription = "Save")
                                }
                                IconButton(onClick = { editing = false }) {
                                    Icon(Icons.Filled.Close, contentDescription = "Cancel")
                                }
                            }
                        }
                    )
                } else {
                    Text(group.minCount.toString())
                }
            }
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
            var editing by remember { mutableStateOf(false) }
            cell(horizontalArrangement = Arrangement.SpaceBetween) {
                Title("Minimum Port")
                if (!editing) {
                    IconButton({ editing = true }, Modifier.pointerHoverIcon(PointerIcon.Hand)) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
                    }
                }
            }
            cell {
                if (editing) {
                    var minPort by remember { mutableStateOf(group.minPort.toString()) }
                    OutlinedTextField(
                        value = minPort,
                        onValueChange = { value -> if (value.all { it.isDigit() }) minPort = value },
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        trailingIcon = {
                            Row {
                                IconButton(onClick = {
                                    coroutineScope.launch {
                                        api.updateGroup(GroupUpdateRequest(name = group.name, minPort = minPort.toIntOrNull() ?: 0))
                                        editing = false
                                    }
                                }) {
                                    Icon(Icons.Filled.Done, contentDescription = "Save")
                                }
                                IconButton(onClick = { editing = false }) {
                                    Icon(Icons.Filled.Close, contentDescription = "Cancel")
                                }
                            }
                        }
                    )
                } else {
                    Text(group.minPort.toString())
                }
            }
        }

        row {
            var editing by remember { mutableStateOf(false) }
            cell(horizontalArrangement = Arrangement.SpaceBetween) {
                Title("Delete on Stop")
                if (!editing) {
                    IconButton({ editing = true }, Modifier.pointerHoverIcon(PointerIcon.Hand)) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
                    }
                }
            }
            cell {
                if (editing) {
                    var deleteOnStop by remember { mutableStateOf(group.deleteOnStop) }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Checkbox(checked = deleteOnStop, onCheckedChange = { deleteOnStop = it })
                        IconButton(onClick = {
                            coroutineScope.launch {
                                api.updateGroup(GroupUpdateRequest(name = group.name, deleteOnStop = deleteOnStop))
                                editing = false
                            }
                        }) {
                            Icon(Icons.Filled.Done, contentDescription = "Save")
                        }
                        IconButton(onClick = { editing = false }) {
                            Icon(Icons.Filled.Close, contentDescription = "Cancel")
                        }
                    }
                } else {
                    Checkbox(checked = group.deleteOnStop, onCheckedChange = {}, enabled = false)
                }
            }
        }

        row {
            var editing by remember { mutableStateOf(false) }
            cell(horizontalArrangement = Arrangement.SpaceBetween) {
                Title("Tags")
                if (!editing) {
                    IconButton({ editing = true }, Modifier.pointerHoverIcon(PointerIcon.Hand)) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
                    }
                }
            }
            cell {
                if (editing) {
                    var tags by remember { mutableStateOf(group.tags.joinToString("\n")) }
                    OutlinedTextField(
                        value = tags,
                        onValueChange = { tags = it },
                        maxLines = 10,
                        trailingIcon = {
                            Row {
                                IconButton(onClick = {
                                    coroutineScope.launch {
                                        api.updateGroup(GroupUpdateRequest(name = group.name, tags = tags.lines().map { it.trim() }.filter { it.isNotEmpty() }.toSet()))
                                        editing = false
                                    }
                                }) {
                                    Icon(Icons.Filled.Done, contentDescription = "Save")
                                }
                                IconButton(onClick = { editing = false }) {
                                    Icon(Icons.Filled.Close, contentDescription = "Cancel")
                                }
                            }
                        }
                    )
                } else {
                    Column {
                        group.tags.forEach { tag -> Text(tag) }
                    }
                }
            }
        }
    }
}