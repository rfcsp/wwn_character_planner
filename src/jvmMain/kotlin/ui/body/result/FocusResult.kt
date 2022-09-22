package ui.body.result

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.map
import ui.model.UiModelController
import ui.utils.asState

@Composable
fun FocusResult(level: Int) {

    val fociState = UiModelController.levelSnapshots[level]!!.map { it.foci }.asState()
    val foci by remember { fociState }

    Column {

        Text(
            text = "Focus:"
        )

        foci.forEach {
            Text(
                text = "- ${it.name}"
            )
        }

    }
}