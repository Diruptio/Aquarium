package diruptio.aquarium.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalClipboard
import diruptio.aquarium.core.Platform
import kotlinx.coroutines.launch

@Composable
fun CopyButton(platform: Platform, text: String) {
    val clipboard = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()
    IconButton(
        onClick = {
            coroutineScope.launch {
                clipboard.setClipEntry(platform.createClipEntry(text))
            }
        },
        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
    ) {
        Icon(imageVector = Icons.Filled.ContentCopy, contentDescription = null)
    }
}
