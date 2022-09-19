package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.map
import ui.body.AttributesComposer
import ui.body.ClassChooser
import ui.body.SkillsChooser
import ui.body.background.BackgroundSelector
import ui.body.background.BackgroundSkillChoice
import ui.body.foci.FociChooser
import ui.body.skill.SkillOverflowAtCreation
import ui.model.UiModelController
import ui.utils.asState

@Composable
fun SheetChoices(
    modifier: Modifier,
) {

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        // Attributes
        AttributesComposer()

        // Background
        BackgroundSelector()

        // Background Skill Choice
        BackgroundSkillChoice()

        // Skills and Pills
        SkillsChooser()

        // Class
        ClassChooser()

        // Foci
        FociChooser()

        // Foci overflow
        SkillOverflowAtCreation()

        // Level Ups
        UiModelController.uiModel
            .levelUpChoices
            .entries
            .sortedBy { it.key }
            .forEach { (level, unspentFlow) ->

                val unspentState = unspentFlow.map { it.unspentSKillPoints }.asState()
                val unspent by remember { unspentState }

                Text(
                    text = "Level $level unspent points $unspent"
                )
            }
    }
}
