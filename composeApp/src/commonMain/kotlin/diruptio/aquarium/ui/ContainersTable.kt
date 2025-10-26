package diruptio.aquarium.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import diruptio.aquarium.ui.component.StatusChip
import diruptio.aquarium.ui.component.Table
import diruptio.aquarium.ui.component.Title
import diruptio.aquarium.ui.component.border
import diruptio.verticallyspinningfish.Container
import diruptio.verticallyspinningfish.VerticallySpinningFishApi

@Composable
fun ContainersTable(api: VerticallySpinningFishApi, openContainerView: (Container) -> Unit) {
    Table(
        cellWidths = listOf(0.4f, 0.3f, 0.3f),
        cellModifier = Modifier.padding(8.dp)
    ) {
        row {
            cell { Title("Name") }
            cell { Title("Group") }
            cell { Title("Status") }
        }

        for (container in api.containers.sortedBy { it.name }) {
            row({
                Modifier
                    .clickable { openContainerView(container) }
                    .pointerHoverIcon(PointerIcon.Hand)
                    .border(topWidth = 1.dp, color = LocalContentColor.current)
            }) {
                cell { Text(container.getPrettyName(api)) }
                cell { Text(api.getGroupByContainer(container.name)?.prettyName ?: "") }
                cell { StatusChip(container.getStatus()) }
            }
        }
    }
}
