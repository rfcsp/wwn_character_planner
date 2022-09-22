package ui.body.skill

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.map
import planner.chacracter.choiceOf
import ui.model.UiModelController
import ui.utils.asState

@Composable
fun SkillOverflowAtCreation() {

    val overflowState = UiModelController.uiModel.map { it.skillOverflows }.asState()
    val overflows by remember { overflowState }
    val uncappedState = UiModelController.uiModel.map { getCreationUncappedSkills(it) }.asState()
    val uncappedSkills by remember { uncappedState }

    Column {
        overflows.forEachIndexed { index, skill ->

            Row {

                Text(
                    text = "Overflow replacement $index:"
                )

                SkillPicker(
                    skillChoice = choiceOf(
                        skills = uncappedSkills.toTypedArray(),
                        name = "UncappedSkills"
                    ),
                    skill = skill,
                    modifier = Modifier.width(IntrinsicSize.Min),
                ) {
                    UiModelController.setOverflow(index, it)
                }

            }

        }

    }
}


