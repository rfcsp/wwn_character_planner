package ui.model

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import planner.chacracter.*
import planner.chacracter.classes.ClassCombo
import planner.chacracter.classes.ClassType
import ui.utils.DefaultCoroutineScope
import ui.utils.share
import ui.utils.state

object UiModelController {

    val uiModel = state(validateModel(UiModel()))

    val levelSnapshots: Map<Int, StateFlow<UiModelLevelSnapshot>> =
        (1..10).associateWith { level ->
            uiModel.map { UiModelLevelSnapshot(it, level) }.share()
        }

    fun setName(name: String) = updateModel {
        it.copy(name = name)
    }

    fun setBackground(background: Background) = updateModel {
        it.copy(background = background)
    }

    fun setSkillSelection(skillSelection: SkillSelection) = updateModel {
        it.copy(skillSelection = skillSelection)
    }

    fun setFreeSkill(skill: Skill) = updateModel {
        it.copy(skillChoices = it.skillChoices.copy(freeSkill = skill))
    }

    fun setQuick1(skill: Skill) = updateModel {
        it.copy(skillChoices = it.skillChoices.copy(quick = it.skillChoices.quick.copy(skill1 = skill)))
    }

    fun setQuick2(skill: Skill) = updateModel {
        it.copy(skillChoices = it.skillChoices.copy(quick = it.skillChoices.quick.copy(skill2 = skill)))
    }

    fun setLearning1(skill: Skill) = updateModel {
        it.copy(skillChoices = it.skillChoices.copy(learning = it.skillChoices.learning.copy(skill1 = skill)))
    }

    fun setLearning2(skill: Skill) = updateModel {
        it.copy(skillChoices = it.skillChoices.copy(learning = it.skillChoices.learning.copy(skill2 = skill)))
    }

    fun setRoll1(rollChoice: RollChoice) = updateModel {
        it.copy(skillChoices = it.skillChoices.copy(roll = it.skillChoices.roll.copy(choice1 = rollChoice)))
    }

    fun setRoll2(rollChoice: RollChoice) = updateModel {
        it.copy(skillChoices = it.skillChoices.copy(roll = it.skillChoices.roll.copy(choice2 = rollChoice)))
    }

    fun setRoll3(rollChoice: RollChoice) = updateModel {
        it.copy(skillChoices = it.skillChoices.copy(roll = it.skillChoices.roll.copy(choice3 = rollChoice)))
    }

    fun setAttribute(attr: Attribute, value: Int) = updateModel {
        val map = it.attributes.toMutableMap()
        map[attr] = value
        it.copy(attributes = map)
    }

    fun setClassCombo(classCombo: ClassCombo) = updateModel {
        it.copy(
            classCombo = if (!classCombo.first.full && classCombo.second == null) {
                classCombo.copy(second = ClassType.values().first())
            } else {
                classCombo
            }
        )
    }

    fun setFociChoices(fociChoice: FociChoice) = updateModel {
        it.copy(fociChoices = fociChoice)
    }

    fun setOverflow(index: Int, skill: Skill) = updateModel {
        val overflows = it.skillOverflows.toMutableList()
        overflows[index] = skill
        it.copy(skillOverflows = overflows)
    }

    fun setSkillChoices(level: Int, skills: List<Skill>) = updateModel {
        val map = it.levelUpChoices.toMutableMap()
        map[level] = map[level]!!.copy(skillBumps = skills)
        it.copy(levelUpChoices = map)
    }

    fun setFocus(level: Int, focus: Focus, skill: Skill?) = updateModel {
        val map = it.levelUpChoices.toMutableMap()
        map[level] = map[level]!!.copy(focus = focus to skill)
        it.copy(levelUpChoices = map)
    }

    fun setAttributeChoices(level: Int, attrs: List<Attribute>) = updateModel {
        val map = it.levelUpChoices.toMutableMap()
        map[level] = map[level]!!.copy(abilityBumps = attrs)
        it.copy(levelUpChoices = map)
    }

    private fun updateModel(action: (UiModel) -> UiModel) =
        DefaultCoroutineScope.launch {
            val value = uiModel.first()
            var newValue = action(value)
            newValue = validateModel(newValue)
            uiModel.emit(newValue)
        }
}
