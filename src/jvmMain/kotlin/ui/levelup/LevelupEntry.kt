package ui.levelup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import planner.chacracter.*
import ui.body.foci.FocusPickRow
import ui.body.skill.SkillPicker
import ui.body.skill.skillMaxLevel
import ui.model.LevelUpChoices
import ui.model.UiModelController
import ui.model.levelHasFoci
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
        text = "Unspent skill points $unspent",
        modifier = Modifier.fillMaxWidth()
            .background(if (unspent < 0) Color.Red else MaterialTheme.colors.onPrimary)
    )
}

@Composable
fun LevelupSkillChoices(level: Int) {
    Column {
        val skillMaxLevel = skillMaxLevel(level)

        val uncappedSkillsState = combine(
            UiModelController.uiModel.skillMaps[level]!!.map { skillMap ->
                skillMap
                    .filterValues { v ->
                        v < skillMaxLevel
                    }
            },
            UiModelController.uiModel.levelUpChoices[level]!!.map { it.unspentSKillPoints },
        ) { map, unspent ->

            map.filterValues {
                skillBumpCostAndMinLevel[it + 1]!!.first < unspent
            }
                .keys
                .toTypedArray()
        }
            .asState()

        val uncappedSkills by remember { uncappedSkillsState }

        val skillChoicesState = UiModelController.uiModel.levelUpChoices[level]!!.map { it.skillBumps }.asState()
        val skillChoices by remember { skillChoicesState }

        val attrChoicesState = UiModelController.uiModel.levelUpChoices[level]!!.map { it.abilityBumps }.asState()
        val attrChoices by remember { attrChoicesState }

        val attrBumpCountState = combine(
            *UiModelController.uiModel.levelUpChoices.filterKeys { it <= level }.values.toTypedArray()
        ) {
            it.flatMap(LevelUpChoices::abilityBumps).size
        }
            .asState()
        val attrBumpCount by remember { attrBumpCountState }

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
                        text = "Remove Skill",
                        color = MaterialTheme.colors.secondary,
                    )
                }
            }
        }

        attrChoices.forEachIndexed { idx, attr ->
            AttrPicker(
                curr = attr,
            ) {
                val attrs = attrChoices.toMutableList()
                it?.run { attrs[idx] = this }
                    ?: attrs.removeAt(idx)
                UiModelController.setAttributeChoices(level, attrs)
            }
        }

        val unspentState = UiModelController.uiModel.levelUpChoices[level]!!.map { it.unspentSKillPoints }.asState()
        val unspent by remember { unspentState }

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
                        text = "Add Skill",
                        color = MaterialTheme.colors.secondary,
                    )
                }
            }

            val availableBumps = AttributeBumpCostAndMinLevel.filterKeys { it <= (attrBumpCount + 1) }

            if (
                attrBumpCount < AttributeBumpCostAndMinLevel.maxOf { it.key }
                &&
                (
                        availableBumps.isNotEmpty()
                                &&
                                level >= availableBumps.maxOf { it.value.second }
                        )
            ) {
                Row {
                    Button(
                        onClick = {
                            val attrs = attrChoices.toMutableList()
                            attrs += Attribute.values().first()
                            UiModelController.setAttributeChoices(level, attrs)
                        },
                    ) {
                        Text(
                            text = "Add Attribute",
                            color = MaterialTheme.colors.secondary,
                        )
                    }
                }
            }

        }

        if (levelHasFoci(level)) {
            FocusLevelPicker(level)
        }
    }
}

@Composable
fun FocusLevelPicker(level: Int) {
    val classComboState = UiModelController.uiModel.classCombo.asState()
    val classCombo by remember { classComboState }

    val focusChoiceState = UiModelController.uiModel.levelUpChoices[level]!!.map { it.focus }.asState()
    val focusChoice by remember { focusChoiceState }

    val previousFociState = UiModelController.levelSnapshots[level - 1]!!.foci.asState()
    val previousFoci by remember { previousFociState }

    FocusPickRow(
        classCombo = classCombo,
        label = "Any: ",
        focusChoice = FocusChoice.Any,
        currentFocus = focusChoice?.first,
        currentSkill = focusChoice?.second,
        previousFocus = previousFoci
    ) { f, s ->
        UiModelController.setFocus(level, f, s)
    }
}

@Composable
fun AttrPicker(
    curr: Attribute,
    onSelect: (Attribute?) -> Unit,
) {

    var expanded by remember { mutableStateOf(false) }

    Row {

        Box {

            Text(
                text = curr.name,
                modifier = Modifier
                    .background(MaterialTheme.colors.secondary)
                    .clickable { expanded = !expanded },
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(IntrinsicSize.Min),
            ) {

                Attribute.values().forEach {
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            onSelect(it)
                        }
                    ) {
                        Text(
                            text = it.name,
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                onSelect(null)
            }
        ) {
            Text(
                text = "Remove Attribute",
                color = MaterialTheme.colors.secondary,
            )
        }

    }
}
