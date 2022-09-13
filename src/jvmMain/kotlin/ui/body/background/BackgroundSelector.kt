package ui.body.background

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import planner.chacracter.Background
import ui.model.UiModelController
import ui.utils.asState

@Composable
fun BackgroundSelector() {
    Row {
        Text(
            text = "Background:",
        )

        BackgroundDropdown(
            modifier = Modifier.width(IntrinsicSize.Min)
        )
    }
}

@Composable
fun BackgroundDropdown(
    modifier: Modifier
) {
    val backgroundState = UiModelController.uiModel.background.asState()
    val background by remember { backgroundState }
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        Text(
            text = background.name,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .clickable { expanded = !expanded },
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(IntrinsicSize.Min),
        ) {
            Background.values()
                .forEach {
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            UiModelController.setBackground(it)
                        },
                    ) {
                        Text(
                            text = it.name,
                        )
                    }
                }
        }
    }
}