package diruptio.aquarium.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Dialog(open: Boolean = true, close: (() -> Unit)? = null, content: @Composable () -> Unit = {}) {
    val scale by animateFloatAsState(if (open) 1f else 0f)
    if (scale > 0) {
        Dialog(
            onDismissRequest = close ?: {},
            properties = DialogProperties(scrimColor = Color.Black.copy(alpha = scale * 0.6f))
        ) {
            Card(Modifier.scale(1f, scale)) {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(16.dp, 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    content()
                }
            }
        }
    }
}
