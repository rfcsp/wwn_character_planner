package ui.body.skill

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.map
import ui.model.UiModelController
import ui.utils.asState

@Composable
fun SkillSelectionLearning() {

    val backgroundState = UiModelController.uiModel.map { it.background }.asState()
    val background by remember { backgroundState }

    Row {
        Text(
            text = "Learning 1:"
        )

        val skillState = UiModelController.uiModel.map { it.skillChoices.learning.skill1 }.asState()
        val skill by remember { skillState }

        SkillPicker(
            skillChoices = background.learning,
            skill = skill,
            modifier = Modifier.width(IntrinsicSize.Min),
            onSkillChosen = {
                UiModelController.setLearning1(it)
            }
        )
    }

    Row {
        Text(
            text = "Learning 2:"
        )

        val skillState = UiModelController.uiModel.map { it.skillChoices.learning.skill2 }.asState()
        val skill by remember { skillState }

        SkillPicker(
            skillChoices = background.learning,
            skill = skill,
            modifier = Modifier.width(IntrinsicSize.Min),
            onSkillChosen = {
                UiModelController.setLearning2(it)
            }
        )
    }
}