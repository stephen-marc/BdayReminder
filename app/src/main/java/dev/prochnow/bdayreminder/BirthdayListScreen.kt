package dev.prochnow.bdayreminder

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.prochnow.bdayreminder.ui.components.AddBirthdayComponent
import dev.prochnow.bdayreminder.ui.theme.CategoryTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun BirthdayListScreen(addBirthdayViewModel: AddBirthDayViewModel = hiltViewModel()) {
    val state by addBirthdayViewModel.viewState.collectAsState()
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetToggle: () -> Unit = {
        coroutineScope.launch {
            addBirthdayViewModel.resetEntry()
            if (sheetState.isVisible) {
                sheetState.hide()
            } else {
                sheetState.show()
            }
        }
    }

    val onCreateEntryClicked: () -> Unit = {
        addBirthdayViewModel.saveEntry()
    }

    ModalBottomSheetLayout(
        sheetContent = {
            AddBirthdaySheetComponent(
                onCreateEntryClicked = onCreateEntryClicked,
                onNameChange = addBirthdayViewModel::updateName,
                nameModel = state.name,
                timeModel = state.time,
                categorySelectionModel = state.category,
                onDateChange = addBirthdayViewModel::updateDate,
                onCategoryChange = addBirthdayViewModel::updateCategory
            )
        },
        sheetShape = MaterialTheme.shapes.large.copy(
            bottomEnd = CornerSize(0.dp),
            bottomStart = CornerSize(0.dp)
        ),
        sheetState = sheetState
    ) {
        Scaffold(
            topBar = { AddBirthdayTopBar() },
            floatingActionButton = {
                AddFloatingActionButton(
                    onClick = bottomSheetToggle,
                    yOffsetFraction = 1 - sheetState.progress.fraction
                )
            },
        ) {

        }
    }
}

@Composable
private fun AddBirthdaySheetComponent(
    nameModel: AddBirthDayViewModel.NameModel,
    timeModel: AddBirthDayViewModel.TimeModel,
    categorySelectionModel: AddBirthDayViewModel.CategorySelectionModel,
    onCreateEntryClicked: () -> Unit,
    onNameChange: (String) -> Unit,
    onDateChange: (LocalDate) -> Unit,
    onCategoryChange: (ColorCategoryModel) -> Unit
) {
    CategoryTheme(categorySelectionModel.selectedColorCategory.categoryModel) {
        TopAppBar(
            title = { Text(stringResource(id = R.string.add_birthday_screen_title)) },
            elevation = 0.dp, actions = {
                AppBarActionIcon(onCreateEntryClicked)
            }
        )
        AddBirthdayComponent(
            nameModel = nameModel,
            timeModel = timeModel,
            categorySelectionModel = categorySelectionModel,
            onNameChange = onNameChange,
            onDateChange = onDateChange,
            onCategoryChange = onCategoryChange
        )
    }
}

@Composable
private fun AppBarActionIcon(onCreateEntryClicked: () -> Unit) {
    IconButton(onClick = onCreateEntryClicked) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(id = R.string.cd_navigate_up),
            tint = LocalContentColor.current
        )
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AddFloatingActionButton(
    onClick: () -> Unit,
    yOffsetFraction: Float
) {
    FloatingActionButton(
        modifier = Modifier.offset(0.dp, 100.dp * yOffsetFraction),
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.cd_add_new_birthday)
        )
    }
}


@Composable
fun AddBirthdayTopBar(onNavigateBackClicked: () -> Unit = { /*TODO*/ }) {
    val topBarBackground = MaterialTheme.colors.primary
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.add_birthday_screen_title)) },
        backgroundColor = topBarBackground,
        navigationIcon = {
            IconButton(onClick = onNavigateBackClicked) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.cd_navigate_up),
                )
            }
        },
    )
}

fun Locale.toJavaLocale(): java.util.Locale = java.util.Locale.forLanguageTag(this.toLanguageTag())
fun Month.localizedName(locale: Locale): String =
    this.getDisplayName(TextStyle.FULL, locale.toJavaLocale())
