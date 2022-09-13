package ui.model

import kotlinx.coroutines.flow.MutableSharedFlow
import planner.chacracter.*
import planner.chacracter.classes.ClassCombo
import planner.chacracter.classes.ClassType
import ui.utils.mutableSharedFlow

data class UiModel(
    val name: MutableSharedFlow<String> = mutableSharedFlow(""),

    val background: MutableSharedFlow<Background> = mutableSharedFlow(Background.values().first()),

    val skillSelection: MutableSharedFlow<SkillSelection> = mutableSharedFlow(SkillSelection.Quick),

    val attributes: MutableSharedFlow<Map<Attribute, Int>> = mutableSharedFlow(Attribute.values().associateWith { 10 }),

    val skillChoices: SkillChoices = SkillChoices(
        freeSkill = mutableSharedFlow(Skill.values().first()),
        quick = SkillChoiceQuick(
            skill1 = mutableSharedFlow(Skill.values().first()),
            skill2 = mutableSharedFlow(Skill.values().first()),
        ),
        learning = SkillChoiceLearning(
            skill1 = mutableSharedFlow(Skill.values().first()),
            skill2 = mutableSharedFlow(Skill.values().first()),
        ),
        roll = SkillChoiceRoll(
            choice1 = mutableSharedFlow(RollChoice(true, null, null)),
            choice2 = mutableSharedFlow(RollChoice(true, null, null)),
            choice3 = mutableSharedFlow(RollChoice(true, null, null)),
        ),
    ),

    val classCombo: MutableSharedFlow<ClassCombo> = mutableSharedFlow(ClassCombo(ClassType.Warrior, null)),

    val fociChoices: MutableSharedFlow<FociChoice> = mutableSharedFlow(
        FociChoice(
            Focus.values().first() to null,
            null,
            null
        )
    ),

    val skillOverflows: MutableSharedFlow<List<Skill>> = mutableSharedFlow(listOf())
) {
    init {
        setupModelConnections(this)
    }
}

data class SkillChoices(
    val freeSkill: MutableSharedFlow<Skill>,
    val quick: SkillChoiceQuick,
    val learning: SkillChoiceLearning,
    val roll: SkillChoiceRoll,
)

data class SkillChoiceQuick(
    val skill1: MutableSharedFlow<Skill>,
    val skill2: MutableSharedFlow<Skill>,
)

data class SkillChoiceLearning(
    val skill1: MutableSharedFlow<Skill>,
    val skill2: MutableSharedFlow<Skill>,
)

typealias RollChoice = Triple<Boolean, // is it growth or learning
        Triple<StatChoice, Attribute, Attribute?>?, // which attributes chosen
        Skill? // maybe it's a skill
        >

data class SkillChoiceRoll(
    val choice1: MutableSharedFlow<RollChoice>,
    val choice2: MutableSharedFlow<RollChoice>,
    val choice3: MutableSharedFlow<RollChoice>,
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

fun defaultFocusChoices(classCombo: ClassCombo): FociChoice {

    // Any is for everyone
    val firstFocus = filterFoci(
        choice = FocusChoice.Any,
        classCombo = classCombo,
        previousFocus = listOf(),
    ).first()

    // A warrior adds another one
    val secondFocus =
        if (classCombo.toList().contains(ClassType.Warrior)) {
            filterFoci(
                choice = FocusChoice.AnyWarrior,
                classCombo = classCombo,
                previousFocus = listOf(firstFocus),
            ).first()
        } else {
            null
        }

    // Expert another
    val thirdFocus =
        if (classCombo.toList().contains(ClassType.Expert)) {
            filterFoci(
                choice = FocusChoice.AnyExpert,
                classCombo = classCombo,
                previousFocus = listOfNotNull(firstFocus, secondFocus),
            ).first()
        } else {
            null
        }

    return FociChoice(
        firstFocus.run { this to skillChoice?.run(::defaultSelect) },
        secondFocus?.run { this to skillChoice?.run(::defaultSelect) },
        thirdFocus?.run { this to skillChoice?.run(::defaultSelect) },
    )
}


