package diruptio.aquarium.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import diruptio.aquarium.ui.component.Table
import diruptio.aquarium.ui.component.Title
import diruptio.aquarium.ui.component.border
import diruptio.verticallyspinningfish.Group
import diruptio.verticallyspinningfish.Status
import diruptio.verticallyspinningfish.VerticallySpinningFishApi

@Composable
fun GroupsTable(api: VerticallySpinningFishApi, openGroupView: (Group) -> Unit) {
    Table(
        cellWidths = listOf(0.2f, 0.3f, 0.2f, 0.3f),
        modifier = Modifier.fillMaxSize(),
        cellModifier = Modifier.padding(8.dp)
    ) {
        row {
            cell { Title("Name") }
            cell { Title("Minimum Instance Count") }
            cell { Title("Instance Count") }
            cell { Title("Available Instance Count") }
        }

        for (group in api.groups.sortedBy { it.name }) {
            row({
                Modifier
                    .clickable { openGroupView(group) }
                    .pointerHoverIcon(PointerIcon.Hand)
                    .border(topWidth = 1.dp, color = LocalContentColor.current)}
            ) {
                val containers = api.getContainersByGroup(group.name)
                cell { Text(group.prettyName) }
                cell { Text(group.minCount.toString()) }
                cell { Text(containers.size.toString()) }
                cell { Text(containers.count { it.getStatus() == Status.AVAILABLE }.toString()) }
            }
        }
    }
}
