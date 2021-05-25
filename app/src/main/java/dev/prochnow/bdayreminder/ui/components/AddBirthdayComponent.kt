package dev.prochnow.bdayreminder.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.prochnow.bdayreminder.AddBirthDayViewModel
import dev.prochnow.bdayreminder.R
import dev.prochnow.bdayreminder.get

@Composable
fun AddBirthdayComponent(
    addBirthdayViewModel: AddBirthDayViewModel
) {
    val state by addBirthdayViewModel.viewState.collectAsState()

    Column(
        modifier = Modifier
            .padding(top = 16.dp, bottom = 40.dp, start = 16.dp, end = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextFieldWithError(
            onTextChange = addBirthdayViewModel::updateName,
            value = state.name.value.get(LocalContext.current),
            isError = state.name.isError,
            errors = state.name.errors.get(LocalContext.current),
            label = { Text(text = stringResource(id = R.string.add_birthday_name_label)) }
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            DatePickerComponent(
                modifier = Modifier.weight(1f),
                onDateSelected = addBirthdayViewModel::updateDate,
                timeModel = state.time
            )
            CategorySelectionDropDown(
                modifier = Modifier
                    .weight(1f),
                categoryModel = state.category,
                onCategoryClick = addBirthdayViewModel::updateCategory
            )
        }
    }
}
