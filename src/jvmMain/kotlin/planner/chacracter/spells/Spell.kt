package planner.chacracter.spells

import planner.chacracter.classes.ClassType

data class Spell(
    val `class`: ClassType,
    val name: String,
    val level: Int,
)
