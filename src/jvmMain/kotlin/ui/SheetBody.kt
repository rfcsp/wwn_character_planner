import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.SheetChoices
import ui.SheetResult

@Composable
fun SheetBody(
    modifier: Modifier
) {
    Row(
        modifier = modifier,
    ) {

        SheetChoices(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
        )

        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(4.dp),
            color = Color.Black,
        )

        SheetResult(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
        )

    }

}

