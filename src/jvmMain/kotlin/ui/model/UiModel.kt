@file:OptIn(DelicateCoroutinesApi::class)

package ui.model

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import planner.chacracter.*
import planner.chacracter.classes.ClassCombo
import planner.chacracter.classes.ClassType
import ui.body.skill.skillMap
import ui.utils.state

class UiModel {
    val name: MutableStateFlow<String> = state("")

    val background: MutableStateFlow<Background> = state(Background.values().first())

    val skillSelection: MutableStateFlow<SkillSelection> = state(SkillSelection.Quick)

    val attributes: MutableStateFlow<Map<Attribute, Int>> = state(Attribute.values().associateWith { 10 })

    val skillChoices: SkillChoices = SkillChoices(
        freeSkill = state(Skill.values().first()),
        quick = SkillChoiceQuick(
            skill1 = state(Skill.values().first()),
            skill2 = state(Skill.values().first()),
        ),
        learning = SkillChoiceLearning(
            skill1 = state(Skill.values().first()),
            skill2 = state(Skill.values().first()),
        ),
        roll = SkillChoiceRoll(
            choice1 = state(RollChoice(true, null, null)),
            choice2 = state(RollChoice(true, null, null)),
            choice3 = state(RollChoice(true, null, null)),
        ),
    )

    val classCombo: MutableStateFlow<ClassCombo> = state(ClassCombo(ClassType.Warrior, null))

    val fociChoices: MutableStateFlow<FociChoice> = state(
        FociChoice(
            Focus.values().first() to null,
            null,
            null
        )
    )

    val skillOverflows: MutableStateFlow<List<Skill>> = state(listOf())

    val levelUpChoices: Map<Int, MutableStateFlow<LevelUpChoices>> = (2..10)
        .associateWith { state(LevelUpChoices(it)) }

    val skillMaps: Map<Int, SharedFlow<Map<Skill, Int>>> by lazy {
        (0..10)
            .associateWith { skillMap(this, it) }
    }

    init {
        setupModelConnections(this)
    }
}

data class SkillChoices(
    val freeSkill: MutableStateFlow<Skill>,
    val quick: SkillChoiceQuick,
    val learning: SkillChoiceLearning,
    val roll: SkillChoiceRoll,
)

data class SkillChoiceQuick(
    val skill1: MutableStateFlow<Skill>,
    val skill2: MutableStateFlow<Skill>,
)

data class SkillChoiceLearning(
    val skill1: MutableStateFlow<Skill>,
    val skill2: MutableStateFlow<Skill>,
)

typealias RollChoice = Triple<Boolean, // is it growth or learning
        Triple<StatChoice, Attribute, Attribute?>?, // which attributes chosen
        Skill? // maybe it's a skill
        >

data class SkillChoiceRoll(
    val choice1: MutableStateFlow<RollChoice>,
    val choice2: MutableStateFlow<RollChoice>,
    val choice3: MutableStateFlow<RollChoice>,
)

fun defaultSelect(skillChoice: SkillChoice) = when (skillChoice) {
    is SingleSkillChoice -> skillChoice.skill
    is MultipleSkillChoice -> skillChoice.skillList.first()
}

typealias FociChoice = Triple<
        Pair<Focus, Skill?>,
        Pair<Focus, Skill?>?,
        Pair<Focus, Skill?>?,
        >

data class LevelUpChoices(
    val level: Int,
    val skillBumps: List<Skill> = listOf(),
    val abilityBumps: List<Attribute> = listOf(),
    val focus: Pair<Focus, Skill?>? = null,
    val unspentSKillPoints: Int = 0,
)
