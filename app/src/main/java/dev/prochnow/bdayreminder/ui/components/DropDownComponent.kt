package dev.prochnow.bdayreminder.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.prochnow.bdayreminder.R
import dev.prochnow.bdayreminder.ui.theme.BdayTheme
import kotlinx.coroutines.flow.collect

@Composable
fun DropDownComponent(
    modifier: Modifier = Modifier,
    label: String = "",
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onDropDownClick: () -> Unit,
    selectedValue: String,
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
            value = selectedValue,
            isError = isError,
            errors = errors,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown, contentDescription = stringResource(
                        id = R.string.cd_show_list_of_elements
                    )
                )
            }
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

@Preview(showBackground = true)
@Composable
fun PreviewDropDown() {
    BdayTheme {
        DropDownComponent(
            expanded = false,
            onDismissRequest = { /*TODO*/ },
            onDropDownClick = { /*TODO*/ },
            selectedValue = "Lorem Ispum",
            isError = false,
            errors = ""
        ) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDropDownError() {
    BdayTheme {
        DropDownComponent(
            expanded = false,
            onDismissRequest = { /*TODO*/ },
            onDropDownClick = { /*TODO*/ },
            selectedValue = "Lorem Ispum",
            isError = true,
            errors = "Error Text"
        ) {

        }
    }
}



