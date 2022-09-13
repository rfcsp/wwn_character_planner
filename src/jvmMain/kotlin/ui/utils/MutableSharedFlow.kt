@file:OptIn(DelicateCoroutinesApi::class)

package ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import planner.chacracter.Background
import planner.chacracter.Focus
import planner.chacracter.Skill
import planner.chacracter.SkillSelection
import planner.chacracter.classes.ClassCombo
import planner.chacracter.classes.ClassType
import ui.model.FociChoice
import ui.model.RollChoice

fun <T> mutableSharedFlow(initialValue: T) =
    MutableSharedFlow<T>(
        replay = 1,
    ).also {
        GlobalScope.launch {
            it.emit(initialValue)
        }
    }

@Composable
@JvmName("asStateString")
fun MutableSharedFlow<String>.asState(): State<String> =
    collectAsState("")

@Composable
@JvmName("asStateMap")
fun <K, V> MutableSharedFlow<Map<K, V>>.asState(): State<Map<K, V>> =
    collectAsState(mapOf())

@Composable
@JvmName("asStateClassCombo")
fun MutableSharedFlow<ClassCombo>.asState(): State<ClassCombo> =
    collectAsState(ClassCombo(ClassType.Warrior, null))

@Composable
@JvmName("asStateSkillSelection")
fun MutableSharedFlow<SkillSelection>.asState(): State<SkillSelection> =
    collectAsState(SkillSelection.Quick)

@Composable
@JvmName("asStateBackground")
fun MutableSharedFlow<Background>.asState(): State<Background> =
    collectAsState(Background.values().first())

@Composable
@JvmName("asStateSkill")
fun MutableSharedFlow<Skill>.asState(): State<Skill> =
    collectAsState(Skill.values().first())

@Composable
@JvmName("asStateFociChoice")
fun MutableSharedFlow<FociChoice>.asState(): State<FociChoice> =
    collectAsState(
        FociChoice(
            Focus.values().first() to Skill.values().first(),
            null,
            null,
        )
    )

@Composable
@JvmName("asStateRollChoice")
fun MutableSharedFlow<RollChoice>.asState(): State<RollChoice> =
    collectAsState(RollChoice(false, null, null))

@Composable
@JvmName("asStateList")
fun <T> MutableSharedFlow<List<T>>.asState(): State<List<T>> =
    collectAsState(listOf())