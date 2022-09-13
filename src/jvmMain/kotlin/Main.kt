import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.SheetHeader

@Preview
@Composable
fun App() {

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            SheetHeader()

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = Color.Black,
            )

            SheetBody(
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}

fun main() = application {
    Window(
        title = "WWN Character Planner",
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}
