package dev.prochnow.bdayreminder.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.prochnow.bdayreminder.AddBirthDayViewModel
import dev.prochnow.bdayreminder.CategoryModel
import dev.prochnow.bdayreminder.R
import dev.prochnow.bdayreminder.ui.get
import java.time.LocalDate

@Composable
fun AddBirthdayComponent(
    nameModel: AddBirthDayViewModel.NameModel,
    timeModel: AddBirthDayViewModel.TimeModel,
    categorySelectionModel: AddBirthDayViewModel.CategorySelectionModel,
    onNameChange: (String) -> Unit,
    onDateChange: (LocalDate) -> Unit,
    onCategoryChange: (CategoryModel) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp, bottom = 40.dp, start = 16.dp, end = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextFieldWithError(
            onTextChange = onNameChange,
            value = nameModel.value.get(LocalContext.current),
            isError = nameModel.isError,
            errors = nameModel.errors.get(LocalContext.current),
            label = { Text(text = stringResource(id = R.string.add_birthday_name_label)) }
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            DatePickerComponent(
                modifier = Modifier.weight(1f),
                onDateSelected = onDateChange,
                timeModel = timeModel
            )
            CategorySelectionDropDown(
                modifier = Modifier
                    .weight(1f),
                categoryModel = categorySelectionModel,
                onCategoryClick = onCategoryChange
            )
        }
    }
}
