package ui.body.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.map
import ui.model.UiModelController
import ui.utils.asState

@Composable
fun SkillsResult(level: Int) {

    val skillMapState = UiModelController.levelSnapshots[level]!!.map { it.skillMap }.asState()
    val skillMap by remember { skillMapState }

    Column {

        Text(
            text = "Skills"
        )

        skillMap
            .forEach { (key, value) ->

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Text(
                        text = key.name,
                        modifier = Modifier
                            .weight(1f)
                    )

                    Text(
                        text = if (value > 0) "+$value" else if (value < 0) "$value" else "0",
                        modifier = Modifier
                            .weight(1f)
                    )
                }

            }

    }

}