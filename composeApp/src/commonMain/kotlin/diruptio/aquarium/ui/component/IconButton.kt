package diruptio.aquarium.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.material3.IconButton as MaterialIconButton

/**
 * An IconButton component that wraps Material3 IconButton with a hand cursor on hover when enabled.
 *
 * @param onClick The callback to be invoked when the button is clicked
 * @param modifier The modifier to be applied to the button
 * @param enabled Controls the enabled state of the button
 * @param colors The colors of the button
 * @param interactionSource The interaction source for the button
 * @param content The content of the button (typically an Icon)
 */
@Composable
fun IconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {
    val buttonModifier = if (enabled) {
        modifier.pointerHoverIcon(PointerIcon.Hand)
    } else {
        modifier
    }
    
    MaterialIconButton(
        onClick = onClick,
        modifier = buttonModifier,
        enabled = enabled,
        colors = colors,
        interactionSource = interactionSource,
        content = content
    )
}
