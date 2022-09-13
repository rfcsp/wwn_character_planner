package ui.body.skill

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import planner.chacracter.MultipleSkillChoice
import planner.chacracter.SingleSkillChoice
import planner.chacracter.Skill
import planner.chacracter.SkillChoice

@Composable
fun SkillPicker(
    skillChoice: SkillChoice,
    skill: Skill,
    modifier: Modifier,
    onSkillChosen: (Skill) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        Text(
            text = skill.name,
            modifier = Modifier
                .fillMaxWidth()
                .background(if (skillChoice is SingleSkillChoice) Color.White else Color.LightGray)
                .clickable(enabled = skillChoice !is SingleSkillChoice) { expanded = !expanded },
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(IntrinsicSize.Min),
        ) {

            when (skillChoice) {
                is SingleSkillChoice -> {
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            onSkillChosen(skillChoice.skill)
                        },
                    ) {
                        Text(
                            text = skillChoice.skill.name,
                        )
                    }
                }

                is MultipleSkillChoice -> {
                    skillChoice.skillList.forEach {
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                                onSkillChosen(it)
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
}

@Composable
fun SkillPicker(
    skillChoices: List<SkillChoice>,
    skill: Skill,
    modifier: Modifier,
    onSkillChosen: (Skill) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        Text(
            text = skill.name,
            modifier = Modifier
                .fillMaxWidth()
                .background(if (skillChoices.size == 1) Color.White else Color.LightGray)
                .clickable { expanded = !expanded },
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(IntrinsicSize.Min),
        ) {

            skillChoices.forEach {

                when (it) {

                    is SingleSkillChoice -> {
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                                onSkillChosen(it.skill)
                            },
                        ) {
                            Text(
                                text = it.skill.name,
                            )
                        }
                    }

                    is MultipleSkillChoice -> {

                        DropdownMenuItem(
                            onClick = {}
                        ) {
                            Text(
                                text = it.name,
                                color = Color.LightGray,
                            )
                        }

                        it.skillList.forEach {
                            DropdownMenuItem(
                                onClick = {
                                    expanded = false
                                    onSkillChosen(it)
                                },
                            ) {
                                Text(
                                    text = "- ${it.name}",
                                )
                            }
                        }
                    }
                }

            }
        }
    }

}