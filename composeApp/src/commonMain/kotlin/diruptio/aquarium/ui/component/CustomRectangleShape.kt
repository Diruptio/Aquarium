package diruptio.aquarium.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomRectangleShape(width: Dp): Shape {
    return CustomRectangleShape(
        horizontalWidth = width,
        verticalWidth = width)
}

@Composable
fun CustomRectangleShape(horizontalWidth: Dp, verticalWidth: Dp): Shape {
    return CustomRectangleShape(
        startWidth = horizontalWidth,
        topWidth = verticalWidth,
        endWidth = horizontalWidth,
        bottomWidth = verticalWidth)
}

@Composable
fun CustomRectangleShape(startWidth: Dp, topWidth: Dp, endWidth: Dp, bottomWidth: Dp): Shape {
    val startWidthInPx = with(LocalDensity.current) { startWidth.toPx() }
    val topWidthInPx = with(LocalDensity.current) { topWidth.toPx() }
    val endWidthInPx = with(LocalDensity.current) { endWidth.toPx() }
    val bottomWidthInPx = with(LocalDensity.current) { bottomWidth.toPx() }

    return GenericShape { size, _ ->
        // Start line
        moveTo(0f, 0f)
        lineTo(startWidthInPx, 0f)
        lineTo(startWidthInPx, size.height)
        lineTo(0f, size.height)

        // Top line
        moveTo(0f, 0f)
        lineTo(size.width, 0f)
        lineTo(size.width, topWidthInPx)
        lineTo(0f, topWidthInPx)

        // End line
        moveTo(size.width, 0f)
        lineTo(size.width, size.height)
        lineTo(size.width - endWidthInPx, size.height)
        lineTo(size.width - endWidthInPx, 0f)

        // Bottom line
        moveTo(0f, size.height)
        lineTo(size.width, size.height)
        lineTo(size.width, size.height - bottomWidthInPx)
        lineTo(0f, size.height - bottomWidthInPx)
    }
}

@Composable
fun Modifier.border(horizontalWidth: Dp = 0.dp, verticalWidth: Dp = 0.dp, color: Color): Modifier {
    return border(
        startWidth = horizontalWidth,
        topWidth = verticalWidth,
        endWidth = horizontalWidth,
        bottomWidth = verticalWidth,
        color = color)
}

@Composable
fun Modifier.border(startWidth: Dp = 0.dp,
                    topWidth: Dp = 0.dp,
                    endWidth: Dp = 0.dp,
                    bottomWidth: Dp = 0.dp,
                    color: Color): Modifier {
    return this
        .border(
            width = startWidth,
            color = color,
            shape = CustomRectangleShape(startWidth, 0.dp, 0.dp, 0.dp))
        .border(
            width = topWidth,
            color = color,
            shape = CustomRectangleShape(0.dp, topWidth, 0.dp, 0.dp))
        .border(
            width = endWidth,
            color = color,
            shape = CustomRectangleShape(0.dp, 0.dp, endWidth, 0.dp))
        .border(
            width = bottomWidth,
            color = color,
            shape = CustomRectangleShape(0.dp, 0.dp, 0.dp, bottomWidth))
}
