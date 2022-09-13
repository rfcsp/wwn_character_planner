@file:OptIn(DelicateCoroutinesApi::class)

package ui.model

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import planner.chacracter.MultipleSkillChoice
import planner.chacracter.SingleSkillChoice
import planner.chacracter.Skill
import planner.chacracter.SkillChoice
import ui.body.skill.skillMap

fun setupModelConnections(model: UiModel) {

    ensureFreeSkillIsValid(model)

    ensureQuickSkillsAreValid(model)

    ensureLearningSkillsAreValid(model)

    resetRollOnBackgroundChange(model)

    resetFocusOnClassChanged(model)

    validateOverflows(model)
}

private fun <T1, T2> combineWithoutRepeat(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    combine: (T1, T2) -> T1,
    onEach: suspend (T1) -> Unit,
) {
    combine(flow1, flow2, combine).distinctUntilChanged().onEach { onEach(it) }
        .flowOn(Dispatchers.Unconfined)
        .launchIn(GlobalScope)
}

private fun ensureFreeSkillIsValid(model: UiModel) = with(model) {
    combineWithoutRepeat(
        skillChoices.freeSkill,
        background.map { it.free },
        ::validateFor,
        skillChoices.freeSkill::emit,
    )
}


private fun ensureQuickSkillsAreValid(model: UiModel) = with(model) {
    combineWithoutRepeat(
        skillChoices.quick.skill1,
        background.map { it.quick[0] },
        ::validateFor,
        skillChoices.quick.skill1::emit,
    )

    combineWithoutRepeat(
        skillChoices.quick.skill2,
        background.map { it.quick[1] },
        ::validateFor,
        skillChoices.quick.skill2::emit,
    )
}

fun ensureLearningSkillsAreValid(model: UiModel) = with(model) {
    combineWithoutRepeat(
        skillChoices.learning.skill1,
        background.map { it.learning },
        ::validateFor,
        skillChoices.learning.skill1::emit,
    )

    combineWithoutRepeat(
        skillChoices.learning.skill2,
        background.map { it.learning },
        ::validateFor,
        skillChoices.learning.skill2::emit,
    )
}

fun resetRollOnBackgroundChange(model: UiModel) = with(model) {
    background.onEach {
        this.skillChoices.roll.choice1.emit(RollChoice(true, null, defaultSelect(it.learning[0])))
        this.skillChoices.roll.choice2.emit(RollChoice(true, null, defaultSelect(it.learning[0])))
        this.skillChoices.roll.choice3.emit(RollChoice(true, null, defaultSelect(it.learning[0])))
    }
        .flowOn(Dispatchers.Unconfined)
        .launchIn(GlobalScope)
}

fun resetFocusOnClassChanged(model: UiModel) = with(model) {
    classCombo.map {
        defaultFocusChoices(it)
    }.onEach(fociChoices::emit)
        .flowOn(Dispatchers.Unconfined)
        .launchIn(GlobalScope)
}

fun validateOverflows(model: UiModel) = with(model) {

    val skillMapFlow = skillMap(model, includeOverflows = false, capLevel1 = false).map(Map<Skill, Int>::toMutableMap)

    val overflowsFlow = model.skillOverflows.map(List<Skill>::toMutableList)

    combine(skillMapFlow, overflowsFlow) { skillMap, overflows ->

        val count = skillMap.values.filter { it > 1 }.sumOf { it - 1 }

        skillMap.filterValues { it > 1 }.keys.forEach {
            overflows.remove(it)
        }

        while (overflows.size > count) {
            overflows.removeLast()
        }

        while (overflows.size < count) {
            skillMap.entries.sortedBy { it.key }.filter { !overflows.contains(it.key) }.first { (_, value) ->
                value < 1
            }.run {
                skillMap[key] = value + 1
                overflows += key
            }
        }

        overflows
    }
        .onEach(model.skillOverflows::emit)
        .flowOn(Dispatchers.Unconfined)
        .launchIn(GlobalScope)

}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
fun validateFor(skill: Skill, choice: SkillChoice): Skill = when (choice) {
    is SingleSkillChoice -> choice.skill
    is MultipleSkillChoice -> if (!choice.skillList.contains(skill)) choice.skillList.first()
    else skill
}

fun validateFor(skill: Skill, choice: List<SkillChoice>): Skill {

    val valid = choice.any {
        isChoiceForSkill(skill, it)
    }

    return if (valid) skill
    else defaultSelect(choice[0])
}

fun isChoiceForSkill(skill: Skill, choice: SkillChoice) = when (choice) {
    is SingleSkillChoice -> skill == choice.skill
    is MultipleSkillChoice -> choice.skillList.contains(skill)
}
