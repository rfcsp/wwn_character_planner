package planner.chacracter

import planner.chacracter.Skill.*

enum class Background {
    Artisan {
        override val free = singleSkill(Craft)

        override val quick = listChoice(Trade, Connect)

        override val growth = listOf(
            AnyStat1,
            PhysicalStat2,
            PhysicalStat2,
            MentalStat2,
            SkillGrowthChoice(singleSkill(Exert)),
            SkillGrowthChoice(AnySkill)
        )

        override val learning = listChoice(
            Connect,
            Convince,
            Craft,
            Craft,
            Exert,
            Know,
            Notice,
            Trade,
        )
    },
    Barbarian {
        override val free = singleSkill(Survive)

        override val quick = listChoice(AnyCombat, Notice)

        override val growth = listOf(
            AnyStat1,
            PhysicalStat2,
            PhysicalStat2,
            MentalStat2,
            SkillGrowthChoice(singleSkill(Exert)),
            SkillGrowthChoice(AnySkill)
        )

        override val learning = listChoice(
            AnyCombat,
            Connect,
            Exert,
            Lead,
            Notice,
            Punch,
            Sneak,
            Survive,
        )
    },
    Carter {
        override val free = singleSkill(Ride)

        override val quick = listChoice(Connect, AnyCombat)

        override val growth = listOf(
            AnyStat1,
            PhysicalStat2,
            PhysicalStat2,
            MentalStat2,
            SkillGrowthChoice(singleSkill(Connect)),
            SkillGrowthChoice(AnySkill)
        )

        override val learning = listChoice(
            AnyCombat,
            Connect,
            Craft,
            Exert,
            Notice,
            Perform,
            Survive,
            Trade,
        )
    },
    Courtesan {
        override val free = singleSkill(Perform)

        override val quick = listChoice(Notice, Connect)

        override val growth = listOf(
            AnyStat1,
            MentalStat2,
            MentalStat2,
            PhysicalStat2,
            SkillGrowthChoice(singleSkill(Connect)),
            SkillGrowthChoice(AnySkill)
        )

        override val learning = listChoice(
            AnyCombat,
            Connect,
            Convince,
            Exert,
            Notice,
            Perform,
            Survive,
            Trade,
        )
    },
    Criminal {
        override val free = singleSkill(Sneak)

        override val quick = listChoice(Connect, Convince)

        override val growth = listOf(
            AnyStat1,
            MentalStat2,
            PhysicalStat2,
            MentalStat2,
            SkillGrowthChoice(singleSkill(Connect)),
            SkillGrowthChoice(AnySkill)
        )

        override val learning = listChoice(
            Administer,
            AnyCombat,
            Connect,
            Convince,
            Exert,
            Notice,
            Sneak,
            Trade,
        )
    },
    Hunter {
        override val free = singleSkill(Shoot)

        override val quick = listChoice(Survive, Sneak)

        override val growth = listOf(
            AnyStat1,
            PhysicalStat2,
            PhysicalStat2,
            MentalStat2,
            SkillGrowthChoice(singleSkill(Exert)),
            SkillGrowthChoice(AnySkill)
        )

        override val learning = listChoice(
            AnyCombat,
            Exert,
            Heal,
            Notice,
            Ride,
            Shoot,
            Sneak,
            Survive,
        )
    },
    Laborer {
        override val free = singleSkill(Work)

        override val quick = listChoice(Connect, Exert)

        override val growth = listOf(
            AnyStat1,
            AnyStat1,
            AnyStat1,
            AnyStat1,
            SkillGrowthChoice(singleSkill(Exert)),
            SkillGrowthChoice(AnySkill)
        )

        override val learning = listChoice(
            Administer,
            AnySkill,
            Connect,
            Convince,
            Craft,
            Exert,
            Ride,
            Work,
        )
    },
    Merchant {
        override val free = singleSkill(Trade)

        override val quick = listChoice(Convince, Connect)

        override val growth = listOf(
            AnyStat1,
            MentalStat2,
            MentalStat2,
            MentalStat2,
            SkillGrowthChoice(singleSkill(Connect)),
            SkillGrowthChoice(AnySkill)
        )

        override val learning = listChoice(
            Administer,
            AnyCombat,
            Connect,
            Convince,
            Craft,
            Know,
            Notice,
            Trade,
        )
    },
    Noble {
        override val free = singleSkill(Lead)

        override val quick = listChoice(Connect, Administer)

        override val growth = listOf(
            AnyStat1,
            MentalStat2,
            MentalStat2,
            MentalStat2,
            SkillGrowthChoice(singleSkill(Connect)),
            SkillGrowthChoice(AnySkill)
        )

        override val learning = listChoice(
            Administer,
            AnyCombat,
            Connect,
            Convince,
            Know,
            Lead,
            Notice,
            Ride,
        )
    },
    Nomad {
        override val free = singleSkill(Ride)

        override val quick = listChoice(Survive, AnyCombat)

        override val growth = listOf(
            AnyStat1,
            PhysicalStat2,
            PhysicalStat2,
            MentalStat2,
            SkillGrowthChoice(singleSkill(Exert)),
            SkillGrowthChoice(AnySkill)
        )

        override val learning = listChoice(
            AnyCombat,
            Connect,
            Exert,
            Lead,
            Notice,
            Ride,
            Survive,
            Trade,
        )
    },
    Peasant {
        override val free = singleSkill(Exert)

        override val quick = listChoice(Sneak, Survive)

        override val growth = listOf(
            AnyStat1,
            PhysicalStat2,
            PhysicalStat2,
            PhysicalStat2,
            SkillGrowthChoice(singleSkill(Exert)),
            SkillGrowthChoice(AnySkill)
        )

        override val learning = listChoice(
            Connect,
            Exert,
            Craft,
            Notice,
            Sneak,
            Survive,
            Trade,
            Work,
        )
    },
    Performer {
        override val free = singleSkill(Perform)

        override val quick = listChoice(Convince, Connect)

        override val growth = listOf(
            AnyStat1,
            MentalStat2,
            PhysicalStat2,
            PhysicalStat2,
            SkillGrowthChoice(singleSkill(Exert)),
            SkillGrowthChoice(AnySkill)
        )

        override val learning = listChoice(
            AnyCombat,
            Connect,
            Exert,
            Notice,
            Perform,
            Perform,
            Sneak,
            Convince,
        )
    },
    Physician {
        override val free = singleSkill(Heal)

        override val quick = listChoice(Know, Notice)

        override val growth = listOf(
            AnyStat1,
            PhysicalStat2,
            MentalStat2,
            MentalStat2,
            SkillGrowthChoice(singleSkill(Connect)),
            SkillGrowthChoice(AnySkill)
        )

        override val learning = listChoice(
            Administer,
            Connect,
            Craft,
            Heal,
            Know,
            Notice,
            Convince,
            Trade,
        )
    },
    Priest {
        override val free = singleSkill(Pray)

        override val quick = listChoice(Convince, Know)

        override val growth = listOf(
            AnyStat1,
            MentalStat2,
            PhysicalStat2,
            MentalStat2,
            SkillGrowthChoice(singleSkill(Connect)),
            SkillGrowthChoice(AnySkill)
        )

        override val learning = listChoice(
            Administer,
            Connect,
            Know,
            Lead,
            Heal,
            Convince,
            Pray,
            Pray,
        )
    },
    Sailor {
        override val free = singleSkill(Sail)

        override val quick = listChoice(Exert, Notice)

        override val growth = listOf(
            AnyStat1,
            PhysicalStat2,
            PhysicalStat2,
            MentalStat2,
            SkillGrowthChoice(singleSkill(Exert)),
            SkillGrowthChoice(AnySkill)
        )

        override val learning = listChoice(
            AnyCombat,
            Connect,
            Craft,
            Exert,
            Heal,
            Notice,
            Perform,
            Sail,
        )
    },
    Scholar {
        override val free = singleSkill(Know)

        override val quick = listChoice(Heal, Administer)

        override val growth = listOf(
            AnyStat1,
            MentalStat2,
            MentalStat2,
            MentalStat2,
            SkillGrowthChoice(singleSkill(Connect)),
            SkillGrowthChoice(AnySkill)
        )

        override val learning = listChoice(
            Administer,
            Heal,
            Craft,
            Know,
            Notice,
            Perform,
            Pray,
            Convince,
        )
    },
    Slave {
        override val free = singleSkill(Sneak)

        override val quick = listChoice(Survive, Exert)

        override val growth = listOf(
            AnyStat1,
            PhysicalStat2,
            PhysicalStat2,
            MentalStat2,
            SkillGrowthChoice(singleSkill(Exert)),
            SkillGrowthChoice(AnySkill)
        )

        override val learning = listChoice(
            Administer,
            AnyCombat,
            AnySkill,
            Connect,
            Exert,
            Sneak,
            Survive,
            Work,
        )
    },
    Soldier {
        override val free = AnyCombat

        override val quick = listChoice(Exert, Survive)

        override val growth = listOf(
            AnyStat1,
            PhysicalStat2,
            PhysicalStat2,
            PhysicalStat2,
            SkillGrowthChoice(singleSkill(Exert)),
            SkillGrowthChoice(AnySkill)
        )

        override val learning = listChoice(
            AnyCombat,
            AnyCombat,
            Exert,
            Lead,
            Notice,
            Ride,
            Sneak,
            Survive,
        )
    },
    Thug {
        override val free = AnyCombat

        override val quick = listChoice(Convince, Connect)

        override val growth = listOf(
            AnyStat1,
            MentalStat2,
            PhysicalStat2,
            PhysicalStat2,
            SkillGrowthChoice(singleSkill(Connect)),
            SkillGrowthChoice(AnySkill)
        )

        override val learning = listChoice(
            AnyCombat,
            AnyCombat,
            Connect,
            Convince,
            Exert,
            Notice,
            Sneak,
            Survive,
        )
    },
    Wanderer {
        override val free = singleSkill(Survive)

        override val quick = listChoice(Sneak, Notice)

        override val growth = listOf(
            AnyStat1,
            PhysicalStat2,
            PhysicalStat2,
            MentalStat2,
            SkillGrowthChoice(singleSkill(Connect)),
            SkillGrowthChoice(AnySkill)
        )

        override val learning = listChoice(
            AnyCombat,
            Connect,
            Notice,
            Perform,
            Ride,
            Sneak,
            Survive,
            Work,
        )
    },
    ;

    abstract val free: SkillChoice

    abstract val quick: List<SkillChoice>

    abstract val growth: List<GrowthChoice>

    abstract val learning: List<SkillChoice>
}

sealed class GrowthChoice

class SkillGrowthChoice(val choice: SkillChoice) : GrowthChoice()

abstract class StatChoice(val allowed: Array<Attribute>) : GrowthChoice() {
    abstract val name: String
}

object AnyStat1 : StatChoice(anyAttribute()) {
    override val name: String = "AnyStat1"
}

object PhysicalStat2 : StatChoice(physicalAttributes()) {
    override val name: String = "PhysicalStat2"
}

object MentalStat2 : StatChoice(nonPhysicalAttributes()) {
    override val name: String = "MentalStat2"
}

enum class SkillSelection {
    Quick,
    Learning,
    Roll,
}