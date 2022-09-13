package ui.body.skill

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import planner.chacracter.*
import ui.model.RollChoice
import ui.model.UiModelController
import ui.model.defaultSelect
import ui.utils.asState

@Composable
fun SkillSelectionRoll() {

    Column {

        Row(
            modifier = Modifier.fillMaxWidth().align(Alignment.Start),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {

            Text(
                text = "Roll 1:"
            )

            val rollChoiceState = UiModelController.uiModel.skillChoices.roll.choice1.asState()
            val choice1 by remember { rollChoiceState }

            GrowthChoosing(
                rollChoice = choice1,
                onSelection = UiModelController::setRoll1,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().align(Alignment.Start),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "Roll 2:"
            )

            val rollChoiceState = UiModelController.uiModel.skillChoices.roll.choice2.asState()
            val choice2 by remember { rollChoiceState }

            GrowthChoosing(
                rollChoice = choice2,
                onSelection = UiModelController::setRoll2,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().align(Alignment.Start),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "Roll 3:"
            )

            val rollChoiceState = UiModelController.uiModel.skillChoices.roll.choice3.asState()
            val choice3 by remember { rollChoiceState }

            GrowthChoosing(
                rollChoice = choice3,
                onSelection = UiModelController::setRoll3,
            )
        }
    }

}

@Composable
fun GrowthChoosing(
    rollChoice: RollChoice,
    onSelection: (RollChoice) -> Unit,
) {
    GrowthLearningPicker(rollChoice, onSelection)

    when (rollChoice.first) {
        false -> GrowthPicker(rollChoice, onSelection)
        true -> LearningPicker(rollChoice, onSelection)
    }
}

@Composable
fun GrowthLearningPicker(
    choice: RollChoice,
    onSelection: (RollChoice) -> Unit,
) {

    val labels = mapOf(
        false to "Growth",
        true to "Learning",
    )

    Box {

        var expanded by remember { mutableStateOf(false) }

        Text(
            text = labels[choice.first]!!,
            modifier = Modifier
                .background(Color.LightGray)
                .clickable { expanded = !expanded }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(IntrinsicSize.Min),
        ) {

            val backgroundState = UiModelController.uiModel.background.asState()
            val background by remember { backgroundState }

            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onSelection(defaultForGrowth(background))
                },
            ) {
                Text(
                    text = labels[false]!!,
                )
            }

            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onSelection(defaultForLearning(background))
                },
            ) {
                Text(
                    text = labels[true]!!,
                )
            }
        }
    }
}

fun defaultForGrowth(background: Background): RollChoice =
    when (val growthChoice = background.growth[0]) {
        is SkillGrowthChoice -> RollChoice(false, null, defaultSelect(growthChoice.choice))

        is StatChoice -> {
            RollChoice(
                false,
                Triple(
                    growthChoice,
                    growthChoice.allowed[0],
                    if (growthChoice == AnyStat1) null else growthChoice.allowed[0]
                ),
                null
            )
        }
    }

fun defaultForLearning(background: Background): RollChoice =
    RollChoice(true, null, defaultSelect(background.learning[0]))

@Composable
fun GrowthPicker(
    choice: RollChoice,
    onSelection: (RollChoice) -> Unit,
) {
    require(!choice.first)

    val backgroundState = UiModelController.uiModel.background.asState()
    val background by remember { backgroundState }

    // Entry Picker
    Box {

        var expanded by remember { mutableStateOf(false) }

        Text(
            text = determineGrowthLabel(choice),
            modifier = Modifier
                .background(Color.LightGray)
                .clickable { expanded = !expanded }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(IntrinsicSize.Min),
        ) {

            background.growth.forEach {

                when (it) {
                    is SkillGrowthChoice -> {
                        when (it.choice) {
                            is SingleSkillChoice -> {
                                DropdownMenuItem(
                                    onClick = {
                                        expanded = false
                                        onSelection(RollChoice(false, null, it.choice.skill))
                                    }
                                ) {
                                    Text(
                                        text = it.choice.skill.name
                                    )
                                }
                            }

                            is MultipleSkillChoice -> {
                                DropdownMenuItem(
                                    onClick = {}
                                ) {
                                    Text(
                                        text = it.choice.name
                                    )
                                }

                                it.choice.skillList.forEach {
                                    DropdownMenuItem(
                                        onClick = {
                                            expanded = false
                                            onSelection(RollChoice(false, null, it))
                                        }
                                    ) {
                                        Text(
                                            text = "- $it"
                                        )
                                    }
                                }
                            }
                        }
                    }

                    is StatChoice -> {
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                                onSelection(
                                    RollChoice(
                                        false,
                                        Triple(it, it.allowed.first(), if (it == AnyStat1) null else it.allowed[0]),
                                        null
                                    )
                                )
                            }
                        ) {
                            Text(
                                text = it.name
                            )
                        }
                    }
                }
            }
        }
    }

    // Stat choice
    choice.second?.let { attrChoice ->
        Column {

            growthStatPicker(
                choice = attrChoice,
                firstAttr = true,
                onSelection = onSelection
            )

            attrChoice.third?.let {
                growthStatPicker(
                    choice = attrChoice,
                    firstAttr = false,
                    onSelection = onSelection
                )
            }
        }
    }

}

@Composable
fun growthStatPicker(
    choice: Triple<StatChoice, Attribute, Attribute?>,
    firstAttr: Boolean,
    onSelection: (RollChoice) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Text(
        text = if (firstAttr) choice.second.name else choice.third!!.name,
        modifier = Modifier
            .background(Color.LightGray)
            .clickable { expanded = !expanded }
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.width(IntrinsicSize.Min),
    ) {
        choice.first.allowed.forEach {
            DropdownMenuItem(
                onClick = {
                    expanded = false

                    onSelection(
                        RollChoice(
                            false,
                            Triple(
                                choice.first,
                                if (firstAttr) it else choice.second,
                                if (!firstAttr) it else choice.third,
                            ),
                            null
                        )
                    )
                }
            ) {
                Text(
                    text = it.name
                )
            }
        }
    }
}

@Composable
fun LearningPicker(
    choice: RollChoice,
    onSelection: (RollChoice) -> Unit,
) {
    require(choice.first)

    val backgroundState = UiModelController.uiModel.background.asState()
    val background by remember { backgroundState }

    SkillPicker(
        skillChoices = background.learning,
        skill = choice.third!!,
        modifier = Modifier.width(IntrinsicSize.Min),
        onSkillChosen = {
            onSelection(RollChoice(true, null, it))
        }
    )
}

fun determineGrowthLabel(choice: RollChoice): String =
    when {
        choice.second != null -> choice.second!!.first.name
        else -> choice.third?.name ?: "CARP"
    }
