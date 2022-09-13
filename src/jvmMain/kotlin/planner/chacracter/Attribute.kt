package planner.chacracter

enum class Attribute(val physical: Boolean = true) {
    Strength,
    Dexterity,
    Constitution,
    Wisdom(physical = false),
    Intelligence(physical = false),
    Charisma(physical = false),
    ;

    companion object {
        const val Min = 3
        const val Max = 18
    }
}

fun attributeModifier(value: Int): Int =
    when {
        value <= Attribute.Min -> -2
        value in 4..7 -> -1
        value in 8..13 -> 0
        value in 14..17 -> 1
        value >= Attribute.Max -> 2
        else -> throw Exception("Invalid attribute value $value")
    }

fun anyAttribute() = Attribute.values()

fun physicalAttributes() = Attribute.values().filter { it.physical }.toTypedArray()

fun nonPhysicalAttributes() = Attribute.values().filter { !it.physical }.toTypedArray()