package dev.prochnow.bdayreminder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import dev.prochnow.bdayreminder.ui.theme.BdayTheme
import kotlinx.coroutines.flow.collect
import org.koin.androidx.compose.getStateViewModel
import java.time.Month
import java.time.format.TextStyle

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AddBirthdayScreen(addBirthdayViewModel: AddBirthDayViewModel = getStateViewModel()) {
    val state by addBirthdayViewModel.viewState.collectAsState()
    val nameState = state.name
    val dayModel = state.day

    Scaffold(
        topBar = { AddBirthdayTopBar() },
        floatingActionButton = { AddFloatingActionButton(addBirthdayViewModel::saveEntry) },
        content = { BirthdayInformationComponent(addBirthdayViewModel) }
    )
}

@Composable
fun BirthdayInformationComponent(
    addBirthdayViewModel: AddBirthDayViewModel = getStateViewModel()
) {
    val state by addBirthdayViewModel.viewState.collectAsState()
    val nameState = state.name
    val dayModel = state.day

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextFieldWithError(state.name, addBirthdayViewModel::updateName)

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            DaySelectionDropDown(
                modifier = Modifier.weight(2f),
                dayModel,
                addBirthdayViewModel::updateDay
            )
            MonthSelectionDropDown(
                modifier = Modifier.weight(3f),
                dayModel,
                addBirthdayViewModel::updateMonth
            )
            OutlinedTextField(
                modifier = Modifier.weight(3f),
                label = { Text(text = stringResource(id = R.string.year_label)) },
                value = state.name.value,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = addBirthdayViewModel::updateName
            )
        }

        CategorySelectionDropDown(
            modifier = Modifier.fillMaxWidth(5f / 8f),
            selectedCategory = state.selectedCategory,
            state.availableCategories,
            onCategoryClick = addBirthdayViewModel::updateCategory
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun TextFieldWithError(
    nameState: ValidatorModel,
    onTextChange: (String) -> Unit,
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    readOnly: Boolean = false,
    singleLine: Boolean = false,
    label: @Composable (() -> Unit)? = null,
) {
    Column() {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = label,
            value = nameState.value,
            singleLine = singleLine,
            isError = nameState.showError,
            onValueChange = onTextChange,
            interactionSource = interactionSource,
            readOnly = readOnly,
        )
        AnimatedVisibility(visible = nameState.showError) {
            Text(
                text = nameState.getErrors(),
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.error
            )
        }

    }
}

@Composable
fun AddFloatingActionButton(onSaveEntryClick: () -> Unit) {
    FloatingActionButton(onClick = onSaveEntryClick) {
        Icon(
            imageVector = Icons.Filled.Done,
            contentDescription = stringResource(id = R.string.cd_create_birthday_entry)
        )
    }
}

@Composable
fun DropDownComponent(
    modifier: Modifier = Modifier,
    label: String = "",
    selectionText: ValidatorModel,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onDropDownClick: () -> Unit,
    menuContent: @Composable ColumnScope.() -> Unit
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
            nameState = selectionText,
            onTextChange = {},
            interactionSource = interactionSource,
            label = { Text(text = label) },
            readOnly = true,
            singleLine = true
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

@Composable
fun DaySelectionDropDown(
    modifier: Modifier,
    selectedValue: ValidatorModel,
    onItemClick: (Int) -> Unit,
    menuContent: @Composable ((Int) -> Unit) = {
        Text(text = it.toString())
    }
) {
    var expandedState by remember { mutableStateOf(false) }
    DropDownComponent(
        modifier = modifier,
        selectionText = selectedValue,
        label = stringResource(id = R.string.day_label),
        expanded = expandedState,
        onDismissRequest = { expandedState = false },
        onDropDownClick = { expandedState = !expandedState }
    ) {
        for (dayOfMonth in 1..31) {
            DropdownMenuItem(onClick = {
                onItemClick(dayOfMonth)
                expandedState = false
            }) {
                menuContent(dayOfMonth)
            }
        }
    }
}

@Composable
fun CategorySelectionDropDown(
    modifier: Modifier,
    selectedCategory: CategoryModel,
    availableCategories: List<CategoryModel>,
    onCategoryClick: (CategoryModel) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val localFocusManager = LocalFocusManager.current
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    expanded = !expanded
                }
            }
        }
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier,
            value = selectedCategory.name,
            leadingIcon = {
                Canvas(modifier = Modifier
                    .size(16.dp), onDraw = {
                    drawCircle(color = selectedCategory.color)
                })
            },
            label = { Text(text = stringResource(id = R.string.birthday_category)) },
            onValueChange = {},
            readOnly = true,
            interactionSource = interactionSource
        )
        DropdownMenu(
            modifier = Modifier.heightIn(100.dp, 240.dp),
            expanded = expanded,
            onDismissRequest = {
                expanded = !expanded
                localFocusManager.clearFocus(true)
            }
        ) {
            availableCategories.forEach {
                DropdownMenuItem(onClick = {
                    onCategoryClick(it)
                    expanded = false
                }) {
                    Canvas(modifier = Modifier
                        .padding(end = 8.dp)
                        .size(16.dp), onDraw = {
                        drawCircle(color = it.color)
                    })
                    Text(text = it.name)
                }
            }
        }
    }
}

@Composable
fun MonthSelectionDropDown(
    modifier: Modifier,
    selectedMonth: ValidatorModel,
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
        selectionText = selectedMonth,
        label = stringResource(id = R.string.month_label),
        expanded = expanded,
        onDismissRequest = { expanded = false },
        onDropDownClick = { expanded = !expanded }
    ) {
        for (month in Month.values()) {
            DropdownMenuItem(onClick = {
                onItemClick(month)
                expanded = false
            }) {
                menuContent(month)
            }
        }
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


@Preview
@Composable
fun PreviewAddBirthdayScreen() {
    BdayTheme {
        AddBirthdayScreen(AddBirthDayViewModel(SavedStateHandle()))
    }
}

fun Locale.toJavaLocale() = java.util.Locale.forLanguageTag(this.toLanguageTag())
fun Month.localizedName(locale: Locale) = this.getDisplayName(TextStyle.FULL, locale.toJavaLocale())
