@file:OptIn(DelicateCoroutinesApi::class)

package ui.model

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import planner.chacracter.Attribute
import planner.chacracter.Skill
import ui.body.skill.skillMap
import ui.utils.mutableSharedFlow

data class UiModelLevelSnapshot(
    val model: UiModel,
    val level: Int,
    val attributes: MutableSharedFlow<Map<Attribute, Int>> = mutableSharedFlow(mapOf()),
    val skillMap: MutableSharedFlow<Map<Skill, Int>> = mutableSharedFlow(mapOf()),
) {

    init {

        model.attributes
            .onEach {
                attributes.emit(it)
            }
            .launchIn(GlobalScope)

        skillMap(
            model = model,
            capLevel1 = true,
        )
            .onEach(skillMap::emit)
            .launchIn(GlobalScope)


    }

}