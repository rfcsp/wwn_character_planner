package ui.body

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.map
import planner.chacracter.Attribute
import ui.model.UiModelController
import ui.utils.asState

@Composable
fun AttributesComposer() {

    val state = UiModelController.uiModel.map { it.attributes }.asState()
    val attrs by remember { state }

    Column {

        Attribute.values()
            .forEach { attr ->

                Row {

                    Text(
                        text = attr.name,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .width(100.dp)
                    )

                    var error by remember { mutableStateOf(false) }

                    TextField(
                        value = attrs[attr].toString(),
                        onValueChange = {
                            when {
                                !it.matches(Regex("\\d{1,3}")) -> error = true
                                else -> {
                                    error = false
                                    UiModelController.setAttribute(attr, it.toInt())
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = error
                    )

                }

            }

    }

}