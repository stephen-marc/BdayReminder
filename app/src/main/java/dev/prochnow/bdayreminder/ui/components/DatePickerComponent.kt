package dev.prochnow.bdayreminder.ui.components

import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.buttons
import com.vanpra.composematerialdialogs.datetime.datepicker.datepicker
import dev.prochnow.bdayreminder.R
import dev.prochnow.bdayreminder.TimeModel
import dev.prochnow.bdayreminder.ui.get
import dev.prochnow.bdayreminder.ui.theme.BdayTheme
import java.time.LocalDate


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DatePickerComponent(
    modifier: Modifier = Modifier,
    timeModel: TimeModel,
    onDateSelected: (LocalDate) -> Unit,
) {
    val dialog = remember { MaterialDialog() }
    var resetFocusState by remember { mutableStateOf(false) }
    dialog.build {
        datepicker(initialDate = timeModel.date ?: LocalDate.now()) {
            onDateSelected(it)
            resetFocusState = true
        }
        buttons {
            positiveButton(res = android.R.string.ok)
            positiveButton(res = android.R.string.cancel)
        }
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    if (resetFocusState) {
        LocalFocusManager.current.clearFocus()
        resetFocusState = false
    }

    TextFieldWithError(
        modifier = modifier.onFocusChanged {
            keyboardController?.hide()
            if (it.isFocused) {
                dialog.show()
            }
        },
        onTextChange = {},
        value = timeModel.value.get(LocalContext.current),
        isError = timeModel.isError,
        errors = timeModel.errors.get(LocalContext.current),
        label = { Text(stringResource(id = R.string.date_label)) }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewDatePickerComponent() {
    BdayTheme {
        DatePickerComponent(onDateSelected = { /*TODO*/ }, timeModel = TimeModel())
    }
}
