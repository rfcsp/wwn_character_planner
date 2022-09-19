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
    val attributes: MutableStateFlow<Map<Attribute, Int>> = state(mapOf()),
    val skillMap: StateFlow<Map<Skill, Int>> = skillMap(
        model = model,
        level = level,
    ),
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
            model.attributes,
            attributeBumps,
        ) { attrs, bumps ->

            val newAttrs = attrs.toMutableMap()

            bumps.forEach { newAttrs.computeIfPresent(it) { _, v -> v + 1 } }
            newAttrs
        }
            .onEach(attributes::emit)
            .launch()

    }

}