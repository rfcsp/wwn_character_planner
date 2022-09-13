package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ui.model.UiModelController

@Composable
fun SheetHeader() {

    val state = UiModelController.uiModel.name.collectAsState("")
    val name by remember { state }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalAlignment = Alignment.Start,
    ) {
        // Name and stuff
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Text(
                text = "Name:",
            )

            TextField(
                value = name,
                onValueChange = UiModelController::setName,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

