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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TextFieldWithError(
    value: String,
    isError: Boolean,
    errors: String,
    modifier: Modifier = Modifier,
    onTextChange: (String) -> Unit,
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    readOnly: Boolean = false,
    singleLine: Boolean = false,
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
