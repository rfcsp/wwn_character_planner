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
    val foci: MutableStateFlow<List<Focus>> = state(listOf()),
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

        var focuses = model.fociChoices.map { t -> t.toList().mapNotNull { it?.first } }

        if (level > 1) {
            val bumps = combine(
                model.levelUpChoices
                    .filterKeys { it <= level }
                    .values
            ) {
                it.mapNotNull { l -> l.focus?.first }
            }


            focuses = combine(focuses, bumps) { start, lvls ->
                start + lvls
            }
        }

        focuses
            .onEach(foci::emit)
            .launch()

    }

}