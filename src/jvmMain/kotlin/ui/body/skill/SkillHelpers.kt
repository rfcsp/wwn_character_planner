@file:OptIn(ExperimentalCoroutinesApi::class)

package ui.body.skill

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import planner.chacracter.Skill
import planner.chacracter.SkillSelection
import ui.model.UiModel
import ui.model.share

private val skillMapCache = mutableMapOf<Int, StateFlow<Map<Skill, Int>>>()

@OptIn(ExperimentalCoroutinesApi::class)
fun skillMap(
    model: UiModel,
    level: Int = 0,
): StateFlow<Map<Skill, Int>> =
    skillMapCache.computeIfAbsent(level) { lvl ->
        runBlocking {
            when (lvl) {
                0,
                1,
                -> {
                    val skillFlows = mutableListOf<Flow<List<Skill>>>()

                    skillFlows += model.skillChoices.freeSkill.map { listOf(it) }

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


                    skillFlows += model.fociChoices.map { foci -> foci.toList().mapNotNull { it?.second } }

                    if (lvl != 0) {
                        skillFlows += model.skillOverflows
                    }

                    combine(*skillFlows.toTypedArray()) { skills: Array<List<Skill>> -> skills.flatMap { it } }
                        .map { skills ->
                            val skillMap = Skill.values().associateWith { -1 }.toMutableMap()

                            skills.forEach { skill -> skillMap.computeIfPresent(skill) { _, v -> v + 1 } }

                            skillMap
                        }
                        .map {
                            // Level 0 is raw before capping
                            when (lvl) {
                                0 -> it
                                1 -> it.mapValues { (_, v) -> if (v > 1) 1 else v }
                                else -> throw Exception("CARP")
                            }
                        }
                        .share()
                }

                else -> {
                    combine(
                        skillMap(model, level = lvl - 1),
                        model.levelUpChoices[lvl]!!.map { it.skillBumps },
                    ) { map, skills ->

                        val newMap = map.toMutableMap()

                        skills.forEach {
                            newMap.computeIfPresent(it) { _, v -> v + 1 }
                        }
                        newMap
                    }
                        .share()
                }
            }
        }
    }


fun getCreationUncappedSkills(model: UiModel) = runBlocking {
    skillMap(model)
        .map { it.filterValues { v -> v < 1 }.keys.sorted() }
        .share()
}
