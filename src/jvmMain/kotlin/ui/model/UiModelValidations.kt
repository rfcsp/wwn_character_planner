@file:OptIn(DelicateCoroutinesApi::class)

package ui.model

import kotlinx.coroutines.DelicateCoroutinesApi
import planner.chacracter.*
import planner.chacracter.classes.ClassCombo
import planner.chacracter.classes.ClassType
import planner.chacracter.classes.skillPointsPerLevel
import ui.body.skill.skillMap
import ui.body.skill.skillMaxLevel

fun validateModel(model: UiModel): UiModel {
    var valid = model
        .ensureFreeSkillIsValid()
        .ensureQuickSkillsAreValid()
        .ensureLearningSkillsAreValid()
        .ensureRollChoicesAreValid()
        .ensureCreationFociAreValid()
        .ensureOverflowsAreValid()

    (2..10).forEach { lvl ->
        valid = valid.validateLevel(lvl)
    }

    return valid
}

private fun UiModel.ensureFreeSkillIsValid(): UiModel =
    copy(
        skillChoices = skillChoices.copy(
            freeSkill = validateFor(skillChoices.freeSkill, background.free)
        )
    )

private fun UiModel.ensureQuickSkillsAreValid(): UiModel =
    copy(
        skillChoices = skillChoices.copy(
            quick = skillChoices.quick.copy(
                skill1 = validateFor(skillChoices.quick.skill1, background.quick[0]),
                skill2 = validateFor(skillChoices.quick.skill2, background.quick[1]),
            )
        )
    )

private fun UiModel.ensureLearningSkillsAreValid() =
    copy(
        skillChoices = skillChoices.copy(
            learning = skillChoices.learning.copy(
                skill1 = validateFor(skillChoices.learning.skill1, background.learning),
                skill2 = validateFor(skillChoices.learning.skill2, background.learning),
            )
        )
    )

private fun UiModel.ensureRollChoicesAreValid() =
    copy(
        skillChoices = skillChoices.copy(
            roll = skillChoices.roll.copy(
                choice1 = validateRollChoice(skillChoices.roll.choice1, background),
                choice2 = validateRollChoice(skillChoices.roll.choice2, background),
                choice3 = validateRollChoice(skillChoices.roll.choice3, background),
            )
        )
    )

private fun validateRollChoice(choice: RollChoice, background: Background): RollChoice =
    if (choice.first) {
        RollChoice(
            first = true,
            second = null,
            third = validateFor(choice.third ?: defaultSelect(background.learning.first()), background.learning)
        )
    } else if (choice.second?.let { background.growth.contains(it.first) } == true) {
        RollChoice(
            first = false,
            second = choice.second,
            third = null,
        )
    } else {
        RollChoice(
            first = true,
            second = null,
            third = validateFor(defaultSelect(background.learning.first()), background.learning),
        )
    }

private fun UiModel.ensureCreationFociAreValid() =
    copy(
        fociChoices = validateFoci(fociChoices, classCombo)
    )

fun validateFoci(
    fociChoices: FociChoice,
    classCombo: ClassCombo,
): FociChoice {

    // Any is for everyone
    val first = validateFoci(
        focus = fociChoices.first.first,
        skill = fociChoices.first.second,
        choice = FocusChoice.Any,
        classCombo = classCombo,
        previousFocus = listOf(),
    )!!

    // A warrior adds another one
    val second = if (classCombo.toList().contains(ClassType.Warrior)) {
        validateFoci(
            focus = fociChoices.second?.first,
            skill = fociChoices.second?.second,
            choice = FocusChoice.AnyWarrior,
            classCombo = classCombo,
            previousFocus = listOf(first.first),
        )
    } else {
        null
    }

    // expert adds another one
    val third = if (classCombo.toList().contains(ClassType.Expert)) {
        validateFoci(
            focus = fociChoices.third?.first,
            skill = fociChoices.third?.second,
            choice = FocusChoice.AnyExpert,
            classCombo = classCombo,
            previousFocus = listOfNotNull(first.first, second?.first),
        )
    } else {
        null
    }

    return FociChoice(first, second, third)
}

private fun validateFoci(
    focus: Focus?,
    skill: Skill?,
    choice: FocusChoice,
    classCombo: ClassCombo,
    previousFocus: List<Focus> = listOf(),
): Pair<Focus, Skill?>? {

    return when {

        (choice == FocusChoice.AnyWarrior && classCombo.toList().none { it == ClassType.Warrior }) ||
                (choice == FocusChoice.AnyExpert && classCombo.toList().none { it == ClassType.Expert }) -> null

        else -> {
            val possibleFoci = filterFoci(
                choice = choice,
                classCombo = classCombo,
                previousFocus = previousFocus,
            )

            return if (focus != null && !possibleFoci.contains(focus)) {
                possibleFoci.first() to defaultSelect(possibleFoci.first().skillChoice)
            } else {
                (focus ?: possibleFoci.first()).let {
                    it to it.skillChoice?.let { c ->
                        validateFor(
                            skill,
                            c
                        )
                    }
                }
            }
        }
    }


}

private fun UiModel.ensureOverflowsAreValid(): UiModel =
    copy(
        skillOverflows = run {
            val skillMap = skillMap(this, level = 0).toMutableMap()

            val overflows = skillOverflows.toMutableList()

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
    )

private fun UiModel.validateLevel(level: Int): UiModel {

    val perLevel = skillPointsPerLevel(classCombo)

    val unspent = levelUpChoices[level - 1]?.unspentSKillPoints ?: 0

    val previousAttributeBumps = when (level) {
        2 -> 0

        else -> {
            levelUpChoices.filterKeys { it < level }.values
                .map { it.abilityBumps }
                .flatten()
                .size
        }
    }

    val clearAbilityBumps = previousAttributeBumps >= AttributeBumpCostAndMinLevel.keys.maxOf { v -> v }

    var availablePoints = perLevel + unspent

    // Focus
    levelUpChoices[level]?.focus?.second?.let { skillFromFocus ->

        val maxSkillLevel = skillMaxLevel(level - 1)

        val currLevel = skillMaps[level - 1]!![skillFromFocus]!!

        availablePoints += when (currLevel) {
            maxSkillLevel -> 3
            -1 -> 0
            0 -> 1
            1 -> 0
            else -> 3
        }
    }

    // Skills
    run {
        availablePoints -= levelUpChoices[level]!!
            .skillBumps
            .distinct()
            .associateWith { skill -> levelUpChoices[level]!!.skillBumps.count { it == skill } }.mapValues {

                var currentCost = 0
                val baseLevel = skillMaps[level - 1]!![it.key]!!

                for (i in 1..it.value) {
                    currentCost += skillBumpCostAndMinLevel[baseLevel + i]?.first
                        ?: skillBumpCostAndMinLevel.values.maxOf { v -> v.first }
                }

                currentCost
            }.values.sum()
    }

    // Attributes
    if (!clearAbilityBumps) {
        var totalBumps = previousAttributeBumps

        levelUpChoices[level]!!.abilityBumps.forEach { _ ->
            totalBumps++

            val cost = AttributeBumpCostAndMinLevel[totalBumps]?.first
                ?: AttributeBumpCostAndMinLevel.values.maxOf { it.first }

            availablePoints -= cost
        }
    }

    val choices = levelUpChoices[level]!!.copy(
        unspentSKillPoints = availablePoints
    )
    val newLevelUpChoices = levelUpChoices.toMutableMap()

    newLevelUpChoices[level] = choices

    return copy(
        levelUpChoices = newLevelUpChoices
    )
}

fun levelHasFoci(level: Int) = when (level) {
    2,
    5,
    7,
    10,
    -> true

    else -> false
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
fun validateFor(skill: Skill?, choice: SkillChoice): Skill = when (choice) {
    is SingleSkillChoice -> choice.skill
    is MultipleSkillChoice -> skill?.let { if (!choice.skillList.contains(skill)) choice.skillList.first() else skill }
        ?: choice.skillList.first()
}

fun validateFor(skill: Skill, choice: List<SkillChoice>): Skill {

    return if (choice.any { isChoiceForSkill(skill, it) })
        skill
    else
        defaultSelect(choice[0])
}

fun isChoiceForSkill(skill: Skill, choice: SkillChoice) = when (choice) {
    is SingleSkillChoice -> skill == choice.skill
    is MultipleSkillChoice -> choice.skillList.contains(skill)
}

fun defaultFocusChoices(classCombo: ClassCombo): FociChoice {

    // Any is for everyone
    val firstFocus = filterFoci(
        choice = FocusChoice.Any,
        classCombo = classCombo,
        previousFocus = listOf(),
    ).first()

    // A warrior adds another one
    val secondFocus = if (classCombo.toList().contains(ClassType.Warrior)) {
        filterFoci(
            choice = FocusChoice.AnyWarrior,
            classCombo = classCombo,
            previousFocus = listOf(firstFocus),
        ).first()
    } else {
        null
    }

    // Expert another
    val thirdFocus = if (classCombo.toList().contains(ClassType.Expert)) {
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