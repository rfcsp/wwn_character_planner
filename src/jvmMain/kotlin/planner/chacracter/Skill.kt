package planner.chacracter

enum class Skill(val combat: Boolean = false) {
    Administer,
    Connect,
    Convince,
    Craft,
    Exert,
    Heal,
    Know,
    Lead,
    Magic,
    Notice,
    Perform,
    Pray,
    Punch(combat = true),
    Ride,
    Sail,
    Shoot(combat = true),
    Sneak,
    Stab(combat = true),
    Survive,
    Trade,
    Work,
    ;
}

fun skillChoiceAny() = Skill.values()

fun skillChoiceCombat() = Skill.values().filter { it.combat }.toTypedArray()

fun skillChoiceNonCombat() = Skill.values().filter { !it.combat }.toTypedArray()

fun skillBumpCostAndMinLevel(newLevel: Int): Pair<Int, Int> =
    when (newLevel) {
        0 -> 1 to 1
        1 -> 2 to 1
        2 -> 3 to 3
        3 -> 4 to 6
        4 -> 5 to 9
        else -> throw Exception("Invalid new skill level $newLevel")
    }

sealed class SkillChoice

class SingleSkillChoice(val skill: Skill) : SkillChoice()

open class MultipleSkillChoice(val skillList: List<Skill>, val name: String) : SkillChoice()

val AnySkill = choiceOf(
    skills = skillChoiceAny(),
    name = "AnySkill"
)

val AnyCombat = choiceOf(
    skills = skillChoiceCombat(),
    name = "AnyCombat"
)

val AnyNonCombat = choiceOf(
    skills = skillChoiceNonCombat(),
    name = "AnyNonCombat"
)

fun singleSkill(skill: Skill): SkillChoice = SingleSkillChoice(skill)

fun choiceOf(vararg skills: Skill, name: String): SkillChoice = MultipleSkillChoice(skills.asList(), name)

fun listChoice(vararg skills: Skill): List<SkillChoice> = skills.map(::singleSkill)

fun listChoice(vararg skills: Any): List<SkillChoice> {
    return skills.map {
        when (it) {
            is SkillChoice -> it
            is Skill -> singleSkill(it)
            else -> throw Exception("Invalid skill choice $it")
        }
    }
}
