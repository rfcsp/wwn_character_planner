@file:OptIn(ExperimentalCoroutinesApi::class)

package ui.body.skill

import kotlinx.coroutines.ExperimentalCoroutinesApi
import planner.chacracter.Skill
import planner.chacracter.SkillSelection
import planner.chacracter.skillBumpCostAndMinLevel
import ui.model.UiModel

fun skillMap(
    model: UiModel,
    level: Int = 0,
): Map<Skill, Int> =
    when (level) {
        0,
        1,
        -> {

            val skills = mutableListOf<Skill>()

            // free
            skills += model.skillChoices.freeSkill

            // pickings
            skills += when (model.skillSelection) {
                // quick
                SkillSelection.Quick -> model.skillChoices.quick.run { listOf(skill1, skill2) }

                // learn
                SkillSelection.Learning -> model.skillChoices.learning.run { listOf(skill1, skill2) }

                // roll
                SkillSelection.Roll -> model.skillChoices.roll.run {
                    listOfNotNull(
                        choice1.third,
                        choice2.third,
                        choice3.third
                    )
                }
            }

            // foci
            skills += model.fociChoices.toList().mapNotNull { it?.second }

            // overflows
            if (level != 0) skills += model.skillOverflows

            // build it
            val skillMap = Skill.values().associateWith { -1 }.toMutableMap()

            skills.forEach { skillMap.computeIfPresent(it) { _, v -> v + 1 } }

            when (level) {
                0 -> skillMap
                1 -> {
                    val skillMaxLevel = skillMaxLevel(level)
                    skillMap.mapValues { (_, v) -> if (v > skillMaxLevel) 1 else v }
                }

                else -> throw Exception("CARP")
            }
        }

        else -> {

            val skillMap = skillMap(model, level - 1).toMutableMap()
            val levelUpChoices = model.levelUpChoices[level]!!

            // focus
            levelUpChoices.focus?.second?.run {
                skillMap.computeIfPresent(this) { _, v ->
                    val skillMaxLevel = skillMaxLevel(level)

                    when (v) {
                        skillMaxLevel -> v
                        -1 -> 1
                        0 -> 1
                        1 -> 2
                        else -> v
                    }
                }
            }

            // skill bumps
            levelUpChoices.skillBumps.forEach {
                skillMap.computeIfPresent(it) { _, v -> v + 1 }
            }

            skillMap
        }
    }

fun skillMaxLevel(level: Int): Int =
    skillBumpCostAndMinLevel
        .filter { it.value.second <= level }
        .maxOf { it.key }


fun getCreationUncappedSkills(model: UiModel) =
    skillMap(model, 0)
        .filterValues { it < 1 }
        .keys
        .sorted()
