package diruptio.aquarium.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import diruptio.verticallyspinningfish.Status

@Composable
fun StatusChip(status: Status) {
    Surface(
        shape = MaterialTheme.shapes.extraSmall,
        color = when (status) {
            Status.OFFLINE -> Color(0xFFDD6666)
            Status.ONLINE -> Color(0xFF66DD66)
            Status.AVAILABLE -> Color(0xFF66DD66)
            Status.UNAVAILABLE -> Color(0xFFDDDD66)
        }
    ) {
        Text(status.name, Modifier.padding(4.dp, 0.dp))
    }
}
