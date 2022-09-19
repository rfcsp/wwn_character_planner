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

fun attributeModifier(value: Int) =
    when {
        value <= Attribute.Min -> -2
        value in 4..7 -> -1
        value in 8..13 -> 0
        value in 14..17 -> 1
        value >= Attribute.Max -> 2
        else -> throw Exception("Invalid attribute value $value")
    }

val AttributeBumpCostAndMinLevel = mapOf(
    1 to (1 to 2),
    2 to (2 to 2),
    3 to (3 to 3),
    4 to (4 to 6),
    5 to (5 to 9),
)

fun anyAttribute() = Attribute.values()

fun physicalAttributes() = Attribute.values().filter { it.physical }.toTypedArray()

fun nonPhysicalAttributes() = Attribute.values().filter { !it.physical }.toTypedArray()