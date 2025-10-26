package diruptio.aquarium.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

class TableScope(val scope: LazyListScope,
                 val rowModifier: Modifier,
                 val cellModifier: Modifier,
                 val cellWidths: List<Float>) {
    fun row(rowModifier: @Composable () -> Modifier = { Modifier },
            cellModifier: @Composable () -> Modifier = { Modifier },
            row: @Composable RowScope.() -> Unit) {
        scope.run {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth() then this@TableScope.rowModifier then rowModifier(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    row(RowScope(
                        this@item,
                        this@TableScope.cellModifier then cellModifier(),
                        cellWidths))
                }
            }
        }
    }
}

class RowScope(val scope: LazyItemScope,
               val cellModifier: Modifier,
               val cellWidths: List<Float>,
               var currentCellIndex: Int = 0) {
    @Composable
    fun cell(horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
             content: @Composable () -> Unit) {
        if (currentCellIndex >= cellWidths.size) {
            error("More cells added than there are cell widths defined")
        }
        scope.run {
            Row(
                modifier = Modifier.fillParentMaxWidth(cellWidths[currentCellIndex]) then cellModifier,
                horizontalArrangement = horizontalArrangement,
                verticalAlignment = Alignment.CenterVertically
            ) {
                content()
            }
        }
        currentCellIndex++
    }

    @Composable
    fun cell(width: Dp, content: @Composable () -> Unit) {
        scope.run {
            Row(
                modifier = Modifier.width(width),
                verticalAlignment = Alignment.CenterVertically
            ) {
                content()
            }
        }
    }
}

@Composable
fun Table(
    cellWidths: List<Float>,
    modifier: Modifier = Modifier,
    rowModifier: Modifier = Modifier,
    cellModifier: Modifier = Modifier,
    rows: TableScope.() -> Unit
) {
    LazyColumn(modifier) {
        rows(TableScope(this, rowModifier, cellModifier, cellWidths))
    }
}
