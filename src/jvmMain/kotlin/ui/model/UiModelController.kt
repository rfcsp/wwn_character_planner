@file:OptIn(DelicateCoroutinesApi::class)

package ui.model

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import planner.chacracter.Attribute
import planner.chacracter.Background
import planner.chacracter.Skill
import planner.chacracter.SkillSelection
import planner.chacracter.classes.ClassCombo
import planner.chacracter.classes.ClassType

object UiModelController {

    val uiModel = UiModel()

    val levelSnapshots: Map<Int, UiModelLevelSnapshot> = run {
        val snapshots = mutableListOf<UiModelLevelSnapshot>()
        (1..10).forEach {
            snapshots += UiModelLevelSnapshot(uiModel, it, previousSnapshot = snapshots.lastOrNull())
        }

        snapshots.associateBy { it.level }
    }

    private val attributeModifications = MutableSharedFlow<Pair<Attribute, Int>>()

    private val skillOverflowModification = MutableSharedFlow<Pair<Int, Skill>>()

    init {
        combine(attributeModifications, uiModel.attributes.map(Map<Attribute, Int>::toMutableMap)) { attr, map ->
            map[attr.first] = attr.second
            map
        }
            .onEach(uiModel.attributes::emit)
            .launch()

        combine(skillOverflowModification, uiModel.skillOverflows.map(List<Skill>::toMutableList)) { skill, overflows ->
            overflows[skill.first] = skill.second
            overflows
        }
            .onEach(uiModel.skillOverflows::emit)
            .launch()
    }

    fun setName(name: String) {
        scope.launch {
            uiModel.name.emit(name)
        }
    }

    fun setBackground(background: Background) {
        scope.launch {
            uiModel.background.emit(background)
        }
    }

    fun setSkillSelection(skillSelection: SkillSelection) {
        scope.launch {
            uiModel.skillSelection.emit(skillSelection)
        }
    }

    fun setFreeSkill(skill: Skill) {
        scope.launch {
            uiModel.skillChoices.freeSkill.emit(skill)
        }

    }

    fun setQuick1(skill: Skill) {
        scope.launch {
            uiModel.skillChoices.quick.skill1.emit(skill)
        }
    }

    fun setQuick2(skill: Skill) {
        scope.launch {
            uiModel.skillChoices.quick.skill2.emit(skill)
        }
    }

    fun setLearning1(skill: Skill) {
        scope.launch {
            uiModel.skillChoices.learning.skill1.emit(skill)
        }
    }

    fun setLearning2(skill: Skill) {
        scope.launch {
            uiModel.skillChoices.learning.skill2.emit(skill)
        }
    }

    fun setRoll1(rollChoice: RollChoice) {
        scope.launch {
            uiModel.skillChoices.roll.choice1.emit(rollChoice)
        }
    }

    fun setRoll2(rollChoice: RollChoice) {
        scope.launch {
            uiModel.skillChoices.roll.choice2.emit(rollChoice)
        }
    }

    fun setRoll3(rollChoice: RollChoice) {
        scope.launch {
            uiModel.skillChoices.roll.choice3.emit(rollChoice)
        }
    }

    fun setAttribute(attr: Attribute, value: Int) {
        scope.launch {
            attributeModifications.emit(attr to value)
        }
    }

    fun setClassCombo(classCombo: ClassCombo) {
        scope.launch {
            uiModel.classCombo.emit(
                if (!classCombo.first.full && classCombo.second == null) {
                    classCombo.copy(second = ClassType.values().first())
                } else {
                    classCombo
                }
            )
        }
    }

    fun setFociChoices(fociChoice: FociChoice) {
        scope.launch {
            uiModel.fociChoices.emit(fociChoice)
        }
    }

    fun setOverflow(index: Int, skill: Skill) {
        scope.launch {
            skillOverflowModification.emit(index to skill)
        }
    }
}

