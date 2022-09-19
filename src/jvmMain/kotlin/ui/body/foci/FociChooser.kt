package ui.body.foci

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
import planner.chacracter.*
import planner.chacracter.classes.ClassCombo
import planner.chacracter.classes.ClassType
import ui.body.skill.SkillPicker
import ui.model.FociChoice
import ui.model.UiModelController
import ui.model.defaultSelect
import ui.utils.asState

@Composable
fun FociChooser() {

    val classComboState = UiModelController.uiModel.classCombo.asState()
    val classCombo by remember { classComboState }
    val fociChoicesState = UiModelController.uiModel.fociChoices.asState()
    val fociChoice by remember { fociChoicesState }

    Column {

        Text(
            text = "Level 1 Focus",
        )

        FocusPickRow(
            classCombo = classCombo,
            label = "Any:",
            focusChoice = FocusChoice.Any,
            currentFocus = fociChoice.first.first,
            currentSkill = fociChoice.first.second,
            previousFocus = listOfNotNull(fociChoice.second?.first, fociChoice.third?.first)
        ) { f, s ->
            UiModelController.setFociChoices(
                FociChoice(
                    f to s,
                    fociChoice.second,
                    fociChoice.third
                )
            )
        }

        if (
            classCombo.toList().any { it == ClassType.Warrior }
            && fociChoice.second != null
        ) {
            FocusPickRow(
                classCombo = classCombo,
                label = "Warrior:",
                focusChoice = FocusChoice.AnyWarrior,
                currentFocus = fociChoice.second!!.first,
                currentSkill = fociChoice.second!!.second,
                previousFocus = listOfNotNull(fociChoice.first.first, fociChoice.third?.first)
            ) { f, s ->
                UiModelController.setFociChoices(
                    FociChoice(
                        fociChoice.first,
                        f to s,
                        fociChoice.third
                    )
                )
            }
        }

        if (classCombo.toList().any { it == ClassType.Expert }) {
            FocusPickRow(
                classCombo = classCombo,
                label = "Expert:",
                focusChoice = FocusChoice.AnyExpert,
                currentFocus = fociChoice.third!!.first,
                currentSkill = fociChoice.third!!.second,
                previousFocus = listOfNotNull(fociChoice.first.first, fociChoice.second?.first)
            ) { f, s ->
                UiModelController.setFociChoices(
                    FociChoice(
                        fociChoice.first,
                        fociChoice.second,
                        f to s
                    )
                )
            }
        }
    }

}

@Composable
fun FocusPickRow(
    classCombo: ClassCombo,
    label: String,
    focusChoice: FocusChoice,
    currentFocus: Focus?,
    currentSkill: Skill?,
    previousFocus: List<Focus>,
    onSelection: (Focus, Skill?) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {

        Text(
            text = label,
        )

        FocusPicker(
            focusAvailable = filterFoci(
                choice = focusChoice,
                classCombo = classCombo,
                previousFocus = previousFocus
            ),
            currentFocus = currentFocus,
        ) { f ->
            onSelection(f, f.skillChoice?.run(::defaultSelect))
        }

        currentFocus?.skillChoice?.apply {

            SkillPicker(
                skillChoice = this,
                skill = currentSkill ?: defaultSelect(this),
                modifier = Modifier.width(IntrinsicSize.Min),
            ) {
                onSelection(currentFocus, it)
            }
        }
    }
}

@Composable
fun FocusPicker(
    focusAvailable: List<Focus>,
    currentFocus: Focus?,
    onSelection: (Focus) -> Unit,
) {

    var expanded by remember { mutableStateOf(false) }

    Box {

        Text(
            text = currentFocus?.name ?: "-----",
            modifier = Modifier
                .background(MaterialTheme.colors.secondary)
                .clickable { expanded = !expanded },
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(IntrinsicSize.Min),
        ) {

            focusAvailable.forEach {
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onSelection(it)
                    }
                ) {
                    Text(
                        text = it.name,
                    )
                }
            }
        }
    }
}
