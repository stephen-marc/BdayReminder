package dev.prochnow.bdayreminder.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.prochnow.bdayreminder.ui.theme.BdayTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TextFieldWithError(
    modifier: Modifier = Modifier,
    value: String,
    isError: Boolean,
    errors: String,
    onTextChange: (String) -> Unit,
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    readOnly: Boolean = false,
    singleLine: Boolean = false,
    trailingIcon: @Composable() (() -> Unit)? = null,
    label: @Composable() (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = label,
            value = value,
            singleLine = singleLine,
            isError = isError,
            onValueChange = onTextChange,
            interactionSource = interactionSource,
            readOnly = readOnly,
            trailingIcon = trailingIcon,
        )
        AnimatedVisibility(visible = isError) {
            Text(
                text = errors,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.error
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTextFieldWithErrorNoError() {
    BdayTheme {
        TextFieldWithError(
            value = "LoremIpum",
            isError = false,
            errors = "",
            onTextChange = {/*TODO*/ }) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTextFieldWithError() {
    BdayTheme {
        TextFieldWithError(
            value = "LoremIpum",
            isError = true,
            errors = "ErrorText",
            onTextChange = {/*TODO*/ }) {

        }
    }
}
