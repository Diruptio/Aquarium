package diruptio.aquarium.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.material3.Button as MaterialButton

/**
 * A Button component that wraps Material3 Button with a hand cursor on hover when enabled.
 *
 * @param onClick The callback to be invoked when the button is clicked
 * @param modifier The modifier to be applied to the button
 * @param enabled Controls the enabled state of the button
 * @param shape The shape of the button
 * @param colors The colors of the button
 * @param elevation The elevation of the button
 * @param border The border of the button
 * @param contentPadding The padding of the content
 * @param interactionSource The interaction source for the button
 * @param content The content of the button
 */
@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) {
    val buttonModifier = if (enabled) {
        modifier.pointerHoverIcon(PointerIcon.Hand)
    } else {
        modifier
    }
    
    MaterialButton(
        onClick = onClick,
        modifier = buttonModifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content
    )
}
