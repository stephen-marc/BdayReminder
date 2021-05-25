package dev.prochnow.bdayreminder.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import arrow.optics.optics
import dev.prochnow.bdayreminder.*
import dev.prochnow.bdayreminder.R
import java.time.Month

@optics
data class SplitTimeModel(
    val day: DayModel = DayModel(),
    val month: MonthModel = MonthModel(),
    val year: LocalizedString = LocalizedString.EmptyString,
) {
    companion object
}


@optics
data class MonthModel(
    override val value: Month? = null,
    override val validate: Boolean = false
) : Validatable<Month?> {
    override val isValid: Boolean
        get() = value != null

    override val errors: LocalizedString
        get() = when (value) {
            null -> LocalizedString.resource(R.string.error_required)
            else -> LocalizedString.empty()
        }

    companion object
}

@optics
data class DayModel(
    override val value: LocalizedString.RawString = LocalizedString.RawString(""),
    override val validate: Boolean = false,
) : Validatable<LocalizedString> {

    override val isValid: Boolean
        get() = value.stringValue.isNotEmpty() && value.stringValue.toIntOrNull() != null

    override val errors: LocalizedString
        get() = when {
            value.stringValue.isEmpty() -> LocalizedString.resource(R.string.error_required)
            value.stringValue.toIntOrNull() == null -> LocalizedString.resource(R.string.error_nan)
            else -> LocalizedString.empty()
        }

    companion object
}

@Composable
private fun DateSelectionComponent(
    timeModel: SplitTimeModel,
    onDayChange: (Int) -> Unit,
    onMonthChange: (Month) -> Unit,
    onYearChange: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        DaySelectionDropDown(
            modifier = Modifier.weight(2f),
            timeModel.day,
            onDayChange
        )
        Spacer(modifier = Modifier.width(16.dp))
        MonthSelectionDropDown(
            modifier = Modifier.weight(3f),
            timeModel.month,
            onMonthChange
        )
        Spacer(modifier = Modifier.width(16.dp))
        OutlinedTextField(
            modifier = Modifier.weight(3f),
            label = { Text(text = stringResource(id = R.string.year_label)) },
            value = timeModel.year.get(LocalContext.current),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = onYearChange
        )
    }
}

@Composable
fun DaySelectionDropDown(
    modifier: Modifier,
    selectedValue: DayModel,
    onItemClick: (Int) -> Unit,
    menuContent: @Composable ((Int) -> Unit) = {
        Text(text = it.toString())
    }
) {
    var expandedState by remember { mutableStateOf(false) }
    DropDownComponent(
        modifier = modifier,
        label = stringResource(id = R.string.day_label),
        expanded = expandedState,
        onDismissRequest = { expandedState = false },
        onDropDownClick = { expandedState = !expandedState },
        menuContent = {
            for (dayOfMonth in 1..31) {
                DropdownMenuItem(onClick = {
                    onItemClick(dayOfMonth)
                    expandedState = false
                }) {
                    menuContent(dayOfMonth)
                }
            }
        },
        text = selectedValue.value.get(LocalContext.current),
        isError = selectedValue.isError,
        errors = selectedValue.errors.get(LocalContext.current)
    )
}

@Composable
fun MonthSelectionDropDown(
    modifier: Modifier,
    selectedMonth: Validatable<Month?>,
    onItemClick: (Month) -> Unit,
    menuContent: @Composable ((Month) -> Unit) = { month ->
        Text(
            text = month.localizedName(Locale.current)
        )
    }
) {
    var expanded by remember { mutableStateOf(false) }
    DropDownComponent(
        modifier = modifier,
        label = stringResource(id = R.string.month_label),
        expanded = expanded,
        onDismissRequest = { expanded = false },
        onDropDownClick = { expanded = !expanded },
        menuContent = {
            for (month in Month.values()) {
                DropdownMenuItem(onClick = {
                    onItemClick(month)
                    expanded = false
                }) {
                    menuContent(month)
                }
            }
        },
        text = selectedMonth.value?.localizedName(Locale.current) ?: "",
        isError = selectedMonth.isError,
        errors = selectedMonth.errors.get(LocalContext.current)
    )
}
