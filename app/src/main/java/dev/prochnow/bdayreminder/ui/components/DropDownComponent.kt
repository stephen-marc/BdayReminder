package dev.prochnow.bdayreminder.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collect

@Composable
fun DropDownComponent(
    modifier: Modifier = Modifier,
    label: String = "",
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onDropDownClick: () -> Unit,
    text: String,
    isError: Boolean,
    errors: String,
    menuContent: @Composable() (ColumnScope.() -> Unit),
) {
    val interactionSource = remember { MutableInteractionSource() }
    val localFocusManager = LocalFocusManager.current
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    onDropDownClick()
                }
            }
        }
    }

    Column(modifier = modifier) {
        TextFieldWithError(
            onTextChange = {},
            interactionSource = interactionSource,
            readOnly = true,
            singleLine = true,
            label = { Text(text = label) },
            value = text,
            isError = isError,
            errors = errors
        )
        DropdownMenu(
            modifier = Modifier.heightIn(100.dp, 240.dp),
            expanded = expanded,
            onDismissRequest = {
                onDismissRequest()
                localFocusManager.clearFocus(true)
            },
            content = menuContent
        )
    }
}

