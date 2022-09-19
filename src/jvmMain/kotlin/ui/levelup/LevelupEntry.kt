package ui.levelup

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.map
import planner.chacracter.choiceOf
import planner.chacracter.skillBumpCostAndMinLevel
import ui.body.skill.SkillPicker
import ui.model.UiModelController
import ui.utils.asState

@Composable
fun LevelupEntry(level: Int) {

    Column {

        Divider(
            thickness = 1.dp,
            color = Color.Black,
        )

        Text(
            text = "Level $level",
        )

        UnspentSkillPoints(level)

        LevelupSkillChoices(level)
    }

}

@Composable
fun UnspentSkillPoints(level: Int) {

    val unspentState = UiModelController.uiModel.levelUpChoices[level]!!.map { it.unspentSKillPoints }.asState()
    val unspent by remember { unspentState }

    Text(
        text = "Unspent skill points $unspent"
    )
}

@Composable
fun LevelupSkillChoices(level: Int) {

    val skillChoicesState = UiModelController.uiModel.levelUpChoices[level]!!.map { it.skillBumps }.asState()
    val skillChoices by remember { skillChoicesState }

    val unspentState = UiModelController.uiModel.levelUpChoices[level]!!.map { it.unspentSKillPoints }.asState()
    val unspent by remember { unspentState }

    val skillMaxLevel = skillBumpCostAndMinLevel
        .filter { it.value.second <= level }
        .keys
        .maxOf { it }

    val uncappedSkillsState = UiModelController.uiModel.skillMaps[level]!!.map { skillMap ->
        skillMap
            .filterValues { v ->
                v < skillMaxLevel
            }
            .keys
            .sorted()
            .toTypedArray()
    }
        .asState()

    val uncappedSkills by remember { uncappedSkillsState }

    Column {
        skillChoices.forEachIndexed { idx, skill ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SkillPicker(
                    skillChoice = choiceOf(
                        skills = uncappedSkills,
                        name = "Uncapped",
                    ),
                    skill = skill,
                    modifier = Modifier.width(IntrinsicSize.Min),
                ) {
                    val skills = skillChoices.toMutableList()
                    skills[idx] = it

                    UiModelController.setSkillChoices(level, skills)
                }

                Button(
                    onClick = {
                        val skills = skillChoices.toMutableList()
                        skills.removeAt(idx)
                        UiModelController.setSkillChoices(level, skills)
                    }
                ) {
                    Text(
                        text = "Remove",
                        color = MaterialTheme.colors.secondary,
                    )
                }
            }
        }

        if (unspent > 0) {

            Row {
                Button(
                    onClick = {
                        val skills = skillChoices.toMutableList()
                        skills.add(uncappedSkills.first())
                        UiModelController.setSkillChoices(level, skills)
                    },
                ) {
                    Text(
                        text = "Add",
                        color = MaterialTheme.colors.secondary,
                    )
                }
            }

        }
    }
}