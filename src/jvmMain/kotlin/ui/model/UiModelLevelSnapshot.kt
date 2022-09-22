@file:OptIn(DelicateCoroutinesApi::class)

package ui.model

import kotlinx.coroutines.DelicateCoroutinesApi
import planner.chacracter.Attribute
import planner.chacracter.Focus
import planner.chacracter.Skill
import ui.body.skill.skillMap

data class UiModelLevelSnapshot(
    val attributes: Map<Attribute, Int>,
    val skillMap: Map<Skill, Int>,
    val foci: List<Focus>,
)

fun UiModelLevelSnapshot(
    model: UiModel,
    level: Int,
) = UiModelLevelSnapshot(
    attributes = attributesFromModel(model, level),
    skillMap = skillMap(model, level),
    foci = fociFromModel(model, level),
)

private fun attributesFromModel(
    model: UiModel,
    level: Int,
): MutableMap<Attribute, Int> {
    val bump = model
        .levelUpChoices
        .filterKeys { it <= level }
        .values
        .map { it.abilityBumps }
        .flatten()

    val attributes = model.attributes.toMutableMap()

    bump.forEach { attributes.computeIfPresent(it) { _, v -> v + 1 } }

    return attributes
}

private fun fociFromModel(
    model: UiModel,
    level: Int,
) =
    model.fociChoices.toList().mapNotNull { it?.first } +

            model
                .levelUpChoices
                .filterKeys { it <= level }
                .values
                .mapNotNull { it.focus }
                .map { it.first }
