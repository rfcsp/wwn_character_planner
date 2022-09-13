@file:OptIn(ExperimentalCoroutinesApi::class)

package ui.body.skill

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import planner.chacracter.Skill
import planner.chacracter.SkillSelection
import ui.model.UiModel


fun skillMap(
    model: UiModel,
    includeFree: Boolean = true,
    includeSkillSelection: Boolean = true,
    includeFocus: Boolean = true,
    includeOverflows: Boolean = true,
    capLevel1: Boolean = true,
): Flow<Map<Skill, Int>> {

    val skillFlows = mutableListOf<Flow<List<Skill>>>()

    if (includeFree) {
        skillFlows += model.skillChoices.freeSkill.map { listOf(it) }
    }

    if (includeSkillSelection) {

        skillFlows += model.skillSelection.map { selection ->
            when (selection) {
                SkillSelection.Quick -> combine(
                    model.skillChoices.quick.skill1,
                    model.skillChoices.quick.skill2
                ) { v1, v2 -> listOf(v1, v2) }

                SkillSelection.Learning -> combine(
                    model.skillChoices.learning.skill1,
                    model.skillChoices.learning.skill2
                ) { v1, v2 -> listOf(v1, v2) }

                SkillSelection.Roll -> combine(
                    model.skillChoices.roll.choice1,
                    model.skillChoices.roll.choice2,
                    model.skillChoices.roll.choice3
                ) { v1, v2, v3 ->
                    listOf(v1, v2, v3)
                }.map { roll ->
                    roll.mapNotNull { it.third }
                }
            }
        }
            .flatMapLatest { it }
    }


    if (includeFocus) {
        skillFlows += model.fociChoices.map { foci -> foci.toList().mapNotNull { it?.second } }
    }

    if (includeOverflows) {
        skillFlows += model.skillOverflows
    }

    return combine(*skillFlows.toTypedArray()) { skills: Array<List<Skill>> -> skills.flatMap { it } }
        .map { skills ->
            val skillMap = Skill.values().associateWith { -1 }.toMutableMap()

            skills.forEach { skill -> skillMap.computeIfPresent(skill) { _, v -> v + 1 } }

            skillMap
        }
        .map {
            if (capLevel1)
                it.mapValues { (_, v) -> if (v > 1) 1 else v }
            else
                it
        }
}

fun getUncappedSkills(model: UiModel) =
    skillMap(model)
        .map { it.filterValues { v -> v < 1 }.keys.sorted() }