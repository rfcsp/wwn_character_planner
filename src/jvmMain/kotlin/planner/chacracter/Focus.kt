package planner.chacracter

import planner.chacracter.Focus.*
import planner.chacracter.Skill.*
import planner.chacracter.classes.ClassCombo
import planner.chacracter.classes.ClassType
import planner.chacracter.classes.mage
import planner.chacracter.classes.spellcaster

enum class Focus {
    Alert1,
    Alert2,
    ArmoredMagic1,
    ArmoredMagic2,
    Armsmaster1,
    Armsmaster2,
    Artisan1,
    Artisan2,
    Assassin1,
    Assassin2,
    Authority1,
    Authority2,
    CloseCombatant1,
    CloseCombatant2,
    Connected1,
    Connected2,
    Cultured1,
    Cultured2,
    DieHard1,
    DieHard2,
    Deadeye1,
    Deadeye2,
    Dealmaker1,
    Dealmaker2,
    DevelopedAttribute,
    DiplomaticGrace1,
    DiplomaticGrace2,
    GiftedChirurgeon1,
    GiftedChirurgeon2,
    Henchkeeper1,
    Henchkeeper2,
    ImperviousDefense1,
    ImperviousDefense2,
    Impostor1,
    Impostor2,
    Lucky1,
    Lucky2,
    Nullifier1,
    Nullifier2,
    Poisoner1,
    Poisoner2,
    Polymath1,
    Polymath2,
    Rider1,
    Rider2,
    ShockingAssault1,
    ShockingAssault2,
    SnipersEye1,
    SnipersEye2,
    SpecialOrigin,
    Specialist1,
    Specialist2,
    SpiritFamiliar1,
    SpiritFamiliar2,
    Trapmaster1,
    Trapmaster2,
    UnarmedCombatant1,
    UnarmedCombatant2,
    UniqueGif,
    ValiantDefender1,
    ValiantDefender2,
    WellMet1,
    WellMet2,
    WhirlwindAssault1,
    WhirlwindAssault2,
    Xenoblooded,
    ;

}

val Focus.combat: Boolean
    get() = when (this) {
        else -> true
    }

val Focus.nonCombat: Boolean
    get() = when (this) {
        else -> true
    }

val Focus.requiresPreviousFocus: Focus?
    get() {
        if (this.name.endsWith("2")) {
            return Focus.valueOf(this.name.replace("2", "1"))
        }
        return null
    }

val Focus.canChooseMultipleTimes: Boolean
    get() = when (this) {
        DevelopedAttribute,

        Specialist1,
        Specialist2,
        -> true

        else -> false
    }

fun Focus.isAllowedWithClass(classes: ClassCombo): Boolean =
    when (this) {
        ArmoredMagic1,
        ArmoredMagic2,
        -> classes.toList().any { it?.spellcaster ?: false }

        Nullifier1,
        Nullifier2,
        -> classes.toList().any { !(it?.mage ?: false) }

        DevelopedAttribute -> classes.toList().any { !(it?.mage ?: false) }

        Polymath1,
        Polymath2,
        -> classes.toList().any { it == ClassType.Expert }

        else -> true
    }

val Focus.skillChoice: SkillChoice?
    get() = when (this) {

        Alert1 -> singleSkill(Notice)

        Armsmaster1 -> singleSkill(Stab)

        Artisan1 -> singleSkill(Craft)

        Assassin1 -> singleSkill(Sneak)

        Authority1 -> singleSkill(Lead)

        CloseCombatant1 -> AnyCombat

        Connected1 -> singleSkill(Connect)

        Cultured1 -> singleSkill(Connect)

        Deadeye1 -> singleSkill(Shoot)

        Dealmaker1 -> singleSkill(Trade)

        DiplomaticGrace1 -> singleSkill(Convince)

        GiftedChirurgeon1 -> singleSkill(Heal)

        Henchkeeper1 -> singleSkill(Lead)

        Impostor1 -> choiceOf(Perform, Sneak, name = "")

        Poisoner1 -> singleSkill(Heal)

        Polymath1 -> AnySkill

        Rider1 -> singleSkill(Ride)

        ShockingAssault1 -> choiceOf(Punch, Stab, name = "")

        SnipersEye1 -> singleSkill(Shoot)

        Specialist1 -> choiceOf(*Skill.values().filter {
            when (it) {
                Magic,
                Stab,
                Shoot,
                Punch,
                -> false

                else -> true
            }
        }.toTypedArray(), name = "")

        Trapmaster1 -> singleSkill(Notice)

        UnarmedCombatant1 -> singleSkill(Punch)

        ValiantDefender1 -> choiceOf(Stab, Punch, name = "")

        WhirlwindAssault1 -> singleSkill(Stab)

        else -> null
    }

fun anyFocus() = Focus.values()

fun anyWarrior() = Focus.values().filter { it.combat }.toTypedArray()

fun anyExpert() = Focus.values().filter { it.nonCombat }.toTypedArray()

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

enum class FocusChoice {
    Any,
    AnyWarrior,
    AnyExpert,
}

val FocusChoice.list: Array<Focus>
    get() = when (this) {
        FocusChoice.Any -> anyFocus()
        FocusChoice.AnyWarrior -> anyWarrior()
        FocusChoice.AnyExpert -> anyExpert()
    }

fun filterFoci(
    choice: FocusChoice,
    classCombo: ClassCombo,
    previousFocus: List<Focus> = listOf(),
): List<Focus> {
    return choice.list
        .filter { it.canChooseMultipleTimes || !previousFocus.contains(it) }
        .filter { it.isAllowedWithClass(classCombo) }
        .filter { it.requiresPreviousFocus == null || previousFocus.contains(it.requiresPreviousFocus) }

}
