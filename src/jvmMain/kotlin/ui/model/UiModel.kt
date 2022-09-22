@file:OptIn(DelicateCoroutinesApi::class)

package ui.model

import kotlinx.coroutines.DelicateCoroutinesApi
import planner.chacracter.*
import planner.chacracter.classes.ClassCombo
import planner.chacracter.classes.ClassType

data class UiModel(
    val name: String = "",

    val background: Background = Background.values().first(),

    val skillSelection: SkillSelection = SkillSelection.Quick,

    val attributes: Map<Attribute, Int> = Attribute.values().associateWith { 8 },

    val skillChoices: SkillChoices = SkillChoices(
        freeSkill = defaultSelect(background.free),
        quick = SkillChoiceQuick(
            skill1 = defaultSelect(background.quick[0]),
            skill2 = defaultSelect(background.quick[1]),
        ),
        learning = SkillChoiceLearning(
            skill1 = defaultSelect(background.learning[0]),
            skill2 = defaultSelect(background.learning[1]),
        ),
        roll = SkillChoiceRoll(
            choice1 = RollChoice(true, null, defaultSelect(background.learning[0])),
            choice2 = RollChoice(true, null, defaultSelect(background.learning[1])),
            choice3 = RollChoice(true, null, defaultSelect(background.learning[2])),
        ),
    ),

    val classCombo: ClassCombo = ClassCombo(ClassType.Warrior, null),

    val fociChoices: FociChoice =
        FociChoice(
            Focus.values()[0] to defaultSelect(Focus.values()[0].skillChoice),
            Focus.values()[1] to defaultSelect(Focus.values()[0].skillChoice),
            null
        ),

    val skillOverflows: List<Skill> = listOf(),

    val levelUpChoices: Map<Int, LevelUpChoices> = (2..10)
        .associateWith { LevelUpChoices(it) },

    val skillMaps: Map<Int, Map<Skill, Int>> = (0..10)
        .associateWith { Skill.values().associateWith { -1 } },
)

data class SkillChoices(
    val freeSkill: Skill,
    val quick: SkillChoiceQuick,
    val learning: SkillChoiceLearning,
    val roll: SkillChoiceRoll,
)

data class SkillChoiceQuick(
    val skill1: Skill,
    val skill2: Skill,
)

data class SkillChoiceLearning(
    val skill1: Skill,
    val skill2: Skill,
)

typealias RollChoice = Triple<Boolean, // is it growth or learning
        Triple<StatChoice, Attribute, Attribute?>?, // which attributes chosen
        Skill? // maybe it's a skill
        >

data class SkillChoiceRoll(
    val choice1: RollChoice,
    val choice2: RollChoice,
    val choice3: RollChoice,
)

fun defaultSelect(skillChoice: SkillChoice) = when (skillChoice) {
    is SingleSkillChoice -> skillChoice.skill
    is MultipleSkillChoice -> skillChoice.skillList.first()
}

@JvmName("defaultSelectNullable")
fun defaultSelect(skillChoice: SkillChoice?) =
    skillChoice?.run {
        when (this) {
            is SingleSkillChoice -> this.skill
            is MultipleSkillChoice -> this.skillList.first()
        }
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

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
