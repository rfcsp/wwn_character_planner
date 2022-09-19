package planner.chacracter.classes

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
enum class ClassType(val full: Boolean = true) {
    Warrior,
    Expert,
    HighMage,
    Necromancer,
    Elementalist,
    Healer(full = false),
    Vowed(full = false),
}

val ClassType.mage: Boolean
    get() = when (this) {
        ClassType.Warrior,
        ClassType.Expert,
        -> false

        else -> true
    }

val ClassType.spellcaster: Boolean
    get() = when (this) {
        ClassType.HighMage,
        ClassType.Necromancer,
        ClassType.Elementalist,
        -> true

        else -> false
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
typealias ClassAbility = String

const val KillingBlow: ClassAbility = "Killing Blow"
const val VeteransLuck: ClassAbility = "Veteran's Luck"
const val MasterfulExpertise: ClassAbility = "Masterful Expertise"
const val QuickLearner: ClassAbility = "Quick Learner"

typealias ClassCombo = Pair<ClassType, ClassType?>

fun classAbilities(classCombo: ClassCombo): List<ClassAbility> {
    return when {
        classCombo.second == null && classCombo.first == ClassType.Warrior -> listOf(KillingBlow, VeteransLuck)

        classCombo.second == null && classCombo.first == ClassType.Expert -> listOf(MasterfulExpertise, QuickLearner)

        classCombo.toList().any { it == ClassType.Expert } -> listOf(QuickLearner)

        else -> listOf()
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
fun healthBonus(level: Int, classCombo: ClassCombo): Int {
    return when {
        classCombo.toList().any { it == ClassType.Warrior } -> 2 * level

        classCombo.toList().any { it == ClassType.Expert } -> 0

        else -> -level
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
fun skillPointsPerLevel(classCombo: ClassCombo): Int {
    return if (classAbilities(classCombo).contains(QuickLearner)) 4
    else 3
}
