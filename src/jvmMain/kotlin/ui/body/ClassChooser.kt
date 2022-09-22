package ui.body

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.map
import planner.chacracter.classes.ClassType
import ui.model.UiModelController
import ui.utils.asState

@Composable
fun ClassChooser() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {

        Text(
            text = "Class:",
        )

        val classComboState = UiModelController.uiModel.map { it.classCombo }.asState()
        val classCombo by remember { classComboState }

        Box {
            var expanded by remember { mutableStateOf(false) }

            Text(
                text = classCombo.first.name,
                modifier = Modifier
                    .background(MaterialTheme.colors.secondary)
                    .clickable {
                        expanded = !expanded
                    }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(IntrinsicSize.Min),
            ) {
                ClassType.values()
                    .forEach {
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                                UiModelController.setClassCombo(classCombo.copy(first = it))
                            },
                        ) {
                            Text(
                                text = it.name,
                            )
                        }
                    }
            }
        }

        Box {

            var expanded by remember { mutableStateOf(false) }

            Text(
                text = classCombo.second?.name ?: "-",
                modifier = Modifier
                    .background(MaterialTheme.colors.secondary)
                    .clickable {
                        expanded = !expanded
                    }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(IntrinsicSize.Min),
            ) {

                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        UiModelController.setClassCombo(classCombo.copy(second = null))
                    },
                ) {
                    Text(
                        text = "-",
                    )
                }

                ClassType.values()
                    .forEach {
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                                UiModelController.setClassCombo(classCombo.copy(second = it))
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

}