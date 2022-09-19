@file:OptIn(DelicateCoroutinesApi::class)

package ui.model

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.*
import planner.chacracter.Attribute
import planner.chacracter.Focus
import planner.chacracter.Skill
import ui.body.skill.skillMap
import ui.utils.state

data class UiModelLevelSnapshot(
    val model: UiModel,
    val level: Int,
    val previousSnapshot: UiModelLevelSnapshot? = null,
    val attributes: MutableStateFlow<Map<Attribute, Int>> = state(mapOf()),
    val skillMap: MutableStateFlow<Map<Skill, Int>> = state(mapOf()),
    val foci: List<Focus> = listOf(),
) {
    init {

        val attributeBumps =
            combine(
                model.levelUpChoices
                    .filterKeys { it <= level }
                    .values
            )
            { arr: Array<LevelUpChoices> ->
                arr.map { it.abilityBumps }.flatten()
            }
                .onEmpty {
                    emit(listOf())
                }

        combine(
            model.attributes.map { it.toMutableMap() },
            attributeBumps,
        ) { attrs, bumps ->
            bumps.forEach { attrs.computeIfPresent(it) { _, v -> v + 1 } }
            attrs
        }
            .onEach {
                attributes.emit(it)
            }
            .launch()

        skillMap(
            model = model,
            level = level,
        )
            .onEach(skillMap::emit)
            .launch()


    }

}