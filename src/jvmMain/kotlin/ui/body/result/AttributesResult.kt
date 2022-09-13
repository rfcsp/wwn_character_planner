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
import planner.chacracter.attributeModifier
import ui.model.UiModelController
import ui.utils.asState

@Composable
fun AttributesResult(level: Int) {

    val attributeState = UiModelController.levelSnapshots[level]!!.attributes.asState()
    val attributes by remember { attributeState }

    Column {

        Text(
            text = "Attributes"
        )

        attributes
            .entries
            .sortedBy { it.key }
            .forEach { (key, value) ->
                Row {
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
                            text = value.toString(),
                            modifier = Modifier
                                .weight(1f)
                        )

                        Text(
                            text = "(${attributeModifier(value)})",
                            modifier = Modifier
                                .weight(1f)
                        )
                    }
                }
            }
    }

}