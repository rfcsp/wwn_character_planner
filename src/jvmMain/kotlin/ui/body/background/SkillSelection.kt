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
import planner.chacracter.SkillSelection
import ui.model.UiModelController
import ui.utils.asState

@Composable
fun BackgroundSkillChoice() {
    Row {
        Text(
            text = "Skill selection:",
        )

        SkillSelection(
            modifier = Modifier.width(IntrinsicSize.Min)
        )
    }
}

@Composable
fun SkillSelection(
    modifier: Modifier,
) {
    val skillSelectionState = UiModelController.uiModel.skillSelection.asState()
    val skillSelection by remember { skillSelectionState }
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier,
    ) {
        Text(
            text = skillSelection.name,
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
            SkillSelection.values()
                .forEach {
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            UiModelController.setSkillSelection(it)
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
