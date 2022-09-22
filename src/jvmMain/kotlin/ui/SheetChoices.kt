package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.body.AttributesComposer
import ui.body.ClassChooser
import ui.body.SkillsChooser
import ui.body.background.BackgroundSelector
import ui.body.background.BackgroundSkillChoice
import ui.body.foci.FociChooser
import ui.body.skill.SkillOverflowAtCreation
import ui.levelup.LevelupEntry

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
        (2..10)
            .forEach { LevelupEntry(it) }
    }
}
