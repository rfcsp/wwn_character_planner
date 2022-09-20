@file:OptIn(DelicateCoroutinesApi::class)

package ui.model

import debugLog
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import planner.chacracter.*
import planner.chacracter.classes.ClassCombo
import planner.chacracter.classes.ClassType
import planner.chacracter.classes.skillPointsPerLevel
import ui.body.skill.skillMap
import ui.body.skill.skillMaxLevel

fun setupModelConnections(model: UiModel) {

    ensureFreeSkillIsValid(model)

    ensureQuickSkillsAreValid(model)

    ensureLearningSkillsAreValid(model)

    resetRollOnBackgroundChange(model)

    resetFocusOnClassChanged(model)

    validateCreationOverflows(model)

    (2..10)
        .forEach { validateLevel(model, it) }
}

private fun <T1, T2> defaultCombine(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    combine: (T1, T2) -> T1,
    onEach: suspend (T1) -> Unit,
) {
    combine(flow1, flow2, combine)

        .onEach { onEach(it) }
        .launch()
}

val dispatcher = Dispatchers.IO

//    Executors.newSingleThreadExecutor { r ->
//    Thread(r).also {
//        it.isDaemon = true
//    }
//}.asCoroutineDispatcher()

val scope = CoroutineScope(dispatcher)

fun <T> Flow<T>.launch() =
    flowOn(dispatcher)
        .launchIn(scope)

fun <T> Flow<T>.share() =
    runBlocking(dispatcher) {
        stateIn(scope)
    }

private fun ensureFreeSkillIsValid(model: UiModel) = with(model) {
    defaultCombine(
        skillChoices.freeSkill,
        background.map { it.free },
        ::validateFor,
        skillChoices.freeSkill::emit,
    )
}


private fun ensureQuickSkillsAreValid(model: UiModel) = with(model) {
    defaultCombine(
        skillChoices.quick.skill1,
        background.map { it.quick[0] },
        ::validateFor,
        skillChoices.quick.skill1::emit,
    )

    defaultCombine(
        skillChoices.quick.skill2,
        background.map { it.quick[1] },
        ::validateFor,
        skillChoices.quick.skill2::emit,
    )
}

fun ensureLearningSkillsAreValid(model: UiModel) = with(model) {
    defaultCombine(
        skillChoices.learning.skill1,
        background.map { it.learning },
        ::validateFor,
        skillChoices.learning.skill1::emit
    )

    defaultCombine(
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
        .launch()
}

fun resetFocusOnClassChanged(model: UiModel) = with(model) {
    classCombo.map {
        defaultFocusChoices(it)
    }.onEach(fociChoices::emit)
        .launch()
}

fun validateCreationOverflows(model: UiModel) = with(model) {

    val skillMapFlow = skillMap(model, level = 0).map(Map<Skill, Int>::toMutableMap)

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
        .launch()

}

fun validateLevel(model: UiModel, level: Int) {

    // How many available this level
    val skillPointsAvailableFlow = model.classCombo.map(::skillPointsPerLevel)

    // How many still unspent from previous level
    val unspentPointsFlow = (
            model.levelUpChoices[level - 1]
                ?.map { it.unspentSKillPoints }
            )
        ?: flow { emit(0) }

    // Previous Ability Bumps
    val previousAttributeBumps =
        when (level) {
            2 -> flow { emit(0) }

            else -> {
                combine(
                    model.levelUpChoices
                        .filterKeys { it < level }
                        .values
                ) { arr ->
                    arr.map { it.abilityBumps }
                }
                    .map {
                        it.flatten().size
                    }
            }
        }

    // Clear any ability bumps if max was achieved already
    combine(
        model.levelUpChoices[level]!!,
        previousAttributeBumps,
    ) { levelChoices, previousBumps ->
        if (previousBumps >= AttributeBumpCostAndMinLevel.keys.maxOf { v -> v }) {
            levelChoices.copy(abilityBumps = listOf())
        } else {
            levelChoices
        }
    }

        .onEach {
            model.levelUpChoices[level]!!.emit(it)
        }
        .launch()

    // Calculate unspent
    val availablePointsFlow = combine(
        unspentPointsFlow,
        skillPointsAvailableFlow,
    ) { unspent, available ->
        unspent + available
    }

        .filterNotNull()

    // Calculate unspent
    combine(
        availablePointsFlow,
        previousAttributeBumps,
        model.skillMaps[level - 1]!!,
        model.levelUpChoices[level]!!,
    )
    { availablePoints, previousBumps, previousSkillMap, levelUpChoices ->

        var newAvailablePoints = availablePoints

        // Focus
        run {
            levelUpChoices.focus?.second?.apply {

                val maxSkillLevel = skillMaxLevel(level - 1)

                val currLevel = previousSkillMap[this]!!

                if (level == 5 && this == Skill.Heal) debugLog("newAvailablePoints = $newAvailablePoints")
                newAvailablePoints += when (currLevel) {
                    maxSkillLevel -> 3
                    -1 -> 0
                    0 -> 1
                    1 -> 0
                    else -> 3
                }
                if (level == 5 && this == Skill.Heal) debugLog("newAvailablePoints = $newAvailablePoints")
            }
        }

        // Skills
        run {

            newAvailablePoints -= levelUpChoices.skillBumps
                .distinct()
                .associateWith { skill -> levelUpChoices.skillBumps.count { it == skill } }
                .mapValues {

                    var currentCost = 0
                    val baseLevel = previousSkillMap[it.key]!!

                    for (i in 1..it.value) {
                        currentCost += skillBumpCostAndMinLevel[baseLevel + i]?.first
                            ?: skillBumpCostAndMinLevel.values.maxOf { v -> v.first }
                    }

                    currentCost
                }
                .values
                .sum()
        }

        // Attributes
        run {
            var totalBumps = previousBumps

            levelUpChoices.abilityBumps.forEach { _ ->
                totalBumps++

                val cost = AttributeBumpCostAndMinLevel[totalBumps]?.first
                    ?: AttributeBumpCostAndMinLevel.values.maxOf { it.first }

                newAvailablePoints -= cost
            }
        }

        levelUpChoices.copy(unspentSKillPoints = newAvailablePoints)
    }
        .onEach {
            model.levelUpChoices[level]!!.emit(it)
        }
        .launch()
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