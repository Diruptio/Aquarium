package diruptio.aquarium.ui.component

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import diruptio.verticallyspinningfish.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun StatusSelectMenu(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    open: Boolean,
    close: () -> Unit = {},
    values: List<Status> = Status.entries.toList(),
    onSelect: suspend (status: Status) -> Unit = {}
) {
    DropdownMenu(expanded = open, onDismissRequest = close) {
        for (status in values) {
            DropdownMenuItem(
                text = { StatusChip(status) },
                onClick = {
                    coroutineScope.launch {
                        onSelect(status)
                        close()
                    }
                }
            )
        }
    }
}
