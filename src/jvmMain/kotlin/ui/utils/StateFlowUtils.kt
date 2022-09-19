@file:OptIn(DelicateCoroutinesApi::class)

package ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import planner.chacracter.Background
import planner.chacracter.Focus
import planner.chacracter.Skill
import planner.chacracter.SkillSelection
import planner.chacracter.classes.ClassCombo
import planner.chacracter.classes.ClassType
import ui.model.FociChoice
import ui.model.RollChoice

fun <T> state(initialValue: T) =
    MutableStateFlow(initialValue)
//    MutableSharedFlow<T>(
//        replay = 1,
//        extraBufferCapacity = 0,
//        onBufferOverflow = BufferOverflow.DROP_OLDEST
//    ).also {
//        scope.launch {
//            it.emit(initialValue)
//        }
//    }

@Composable
@JvmName("asStateString")
fun Flow<String>.asState(): State<String> =
    collectAsState("")

@Composable
@JvmName("asStateInt")
fun Flow<Int>.asState(): State<Int> =
    collectAsState(0)

@Composable
@JvmName("asStateMap")
fun <K, V> Flow<Map<K, V>>.asState(): State<Map<K, V>> =
    collectAsState(mapOf())

@Composable
@JvmName("asStateClassCombo")
fun Flow<ClassCombo>.asState(): State<ClassCombo> =
    collectAsState(ClassCombo(ClassType.Warrior, null))

@Composable
@JvmName("asStateSkillSelection")
fun Flow<SkillSelection>.asState(): State<SkillSelection> =
    collectAsState(SkillSelection.Quick)

@Composable
@JvmName("asStateBackground")
fun Flow<Background>.asState(): State<Background> =
    collectAsState(Background.values().first())

@Composable
@JvmName("asStateSkill")
fun Flow<Skill>.asState(): State<Skill> =
    collectAsState(Skill.values().first())

@Composable
@JvmName("asStateFociChoice")
fun Flow<FociChoice>.asState(): State<FociChoice> =
    collectAsState(
        FociChoice(
            Focus.values().first() to Skill.values().first(),
            null,
            null,
        )
    )

@Composable
@JvmName("asStateRollChoice")
fun Flow<RollChoice>.asState(): State<RollChoice> =
    collectAsState(RollChoice(false, null, null))

@Composable
@JvmName("asStateList")
fun <T> Flow<List<T>>.asState(): State<List<T>> =
    collectAsState(listOf())

@Composable
@JvmName("asStateArray")
inline fun <reified T> Flow<Array<T>>.asState(): State<Array<T>> =
    collectAsState(arrayOf())

@Composable
@JvmName("asStatePair")
fun <L, R> Flow<Pair<L, R>?>.asState(): State<Pair<L, R>?> =
    collectAsState(null)
