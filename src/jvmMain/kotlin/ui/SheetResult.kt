package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.body.result.AttributesResult
import ui.body.result.SkillsResult

@Composable
fun SheetResult(
    modifier: Modifier
) {
    var level by remember { mutableStateOf(1) }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {

        // Level Picker
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box {
                var expanded by remember { mutableStateOf(false) }

                Text(
                    text = "Level $level",
                    modifier = Modifier
                        .background(Color.LightGray)
                        .clickable {
                            expanded = !expanded
                        }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.width(IntrinsicSize.Min),
                ) {
                    (1..10)
                        .forEach {
                            DropdownMenuItem(
                                onClick = {
                                    expanded = false
                                    level = it
                                }
                            ) {
                                Text(
                                    text = "Level $it"
                                )
                            }
                        }
                }
            }
        }

        // Attributes
        AttributesResult(level)

        // Skills
        SkillsResult(level)

    }
}