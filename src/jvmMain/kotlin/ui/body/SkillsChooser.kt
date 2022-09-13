package ui.body

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import planner.chacracter.SkillSelection
import ui.body.skill.SkillPicker
import ui.body.skill.SkillSelectionLearning
import ui.body.skill.SkillSelectionQuick
import ui.body.skill.SkillSelectionRoll
import ui.model.UiModelController
import ui.utils.asState

@Composable
fun SkillsChooser() {

    val skillSelectionState = UiModelController.uiModel.skillSelection.asState()
    val skillSelection by remember { skillSelectionState }
    val backgroundState = UiModelController.uiModel.background.asState()
    val background by remember { backgroundState }

    // Free is always there
    Row {
        Text(
            text = "Free:"
        )

        val skillState = UiModelController.uiModel.skillChoices.freeSkill.asState()
        val freeSkill by remember { skillState }

        SkillPicker(
            skillChoice = background.free,
            skill = freeSkill,
            modifier = Modifier.width(IntrinsicSize.Min),
            onSkillChosen = {
                UiModelController.setFreeSkill(it)
            }
        )
    }

    when (skillSelection) {
        SkillSelection.Quick -> SkillSelectionQuick()

        SkillSelection.Learning -> SkillSelectionLearning()

        SkillSelection.Roll -> SkillSelectionRoll()
    }

}

