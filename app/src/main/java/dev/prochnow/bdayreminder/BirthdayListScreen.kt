package dev.prochnow.bdayreminder

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.prochnow.bdayreminder.ui.components.AddBirthdayComponent
import dev.prochnow.bdayreminder.ui.get
import dev.prochnow.bdayreminder.ui.theme.CategoryTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun BirthdayListScreen(
    addBirthdayViewModel: AddBirthDayViewModel,
    birthdayListViewModel: BirthdayListViewModel
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )
    val context = LocalContext.current

    val addState by addBirthdayViewModel.viewState.collectAsState()
    val listState by birthdayListViewModel.viewState.collectAsState()

    val onShowSnackbar: (String) -> Unit = {
        coroutineScope.launch {
            scaffoldState.snackbarHostState.showSnackbar(message = it)
        }
    }
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

    addBirthdayViewModel.eventsFlow.onEach { event ->
        when (event) {
            AddBirthDayViewModel.Event.EntryCreated -> sheetState.hide()
            is AddBirthDayViewModel.Event.ShowSnackBar -> onShowSnackbar(event.message.get(context))
        }
    }.launchIn(coroutineScope)

    ModalBottomSheetLayout(
        sheetContent = {
            AddBirthdaySheetComponent(
                onCreateEntryClicked = addBirthdayViewModel::saveEntry,
                onNameChange = addBirthdayViewModel::updateName,
                nameModel = addState.name,
                timeModel = addState.time,
                categorySelectionModel = addState.category,
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
            scaffoldState = scaffoldState,
            topBar = { AddBirthdayTopBar() },
            floatingActionButton = {
                AddFloatingActionButton(
                    onClick = bottomSheetToggle,
                    yOffsetFraction = 1 - sheetState.progress.fraction
                )
            },
        ) {

            when (val state = listState) {
                is BirthdayListViewModel.BirthdateListViewState.ContentState -> BirthdateList(
                    birthdates = state.list
                )
                BirthdayListViewModel.BirthdateListViewState.EmptyState -> {
                }
                BirthdayListViewModel.BirthdateListViewState.InitialState -> {
                }
            }

        }
    }
}

@Composable
fun BirthdateList(birthdates: List<BirthdayListViewModel.BirthdateModel>) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(birthdates) { item ->
            BirthdateCard(
                title = item.title.get(LocalContext.current),
                date = item.date.get(
                    LocalContext.current
                ), category = item.category
            )
        }
    }
}

@Preview(device = Devices.PIXEL_4)
@Composable
fun PreviewBirthdateCard() {
    CategoryTheme {
        BirthdateCard("Stephens 34th birthday", "Monday, 23.06.1987", CategoryModel.FRIENDS)
    }
}

@Composable
fun BirthdateCard(
    title: String,
    date: String,
    category: CategoryModel
) {
    var expanded by remember { mutableStateOf(false) }

    val offset by animateIntAsState(
        targetValue = if (expanded) {
            48
        } else {
            0
        }
    )

    CategoryTheme(colorPalette = category) {
        Box(modifier = Modifier) {
            Card(
                modifier = Modifier
                    .padding(top = offset.dp)
                    .height(64.dp)
                    .fillMaxWidth(),
                backgroundColor = android.graphics.Color.HSVToColor(FloatArray(3).apply {
                    android.graphics.Color.colorToHSV(MaterialTheme.colors.primary.toArgb(), this)
                    this[2] *= 0.7f
                }).let {
                    Color(it)
                },
                elevation = 0.dp
            ) {
                CompositionLocalProvider(LocalContentColor provides (MaterialTheme.colors.onPrimary)) {
                    Row(
                        Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = stringResource(id = R.string.cd_add_new_birthday)
                            )
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = stringResource(id = R.string.cd_add_new_birthday)
                            )
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expanded = !expanded
                    },
                backgroundColor = MaterialTheme.colors.primary,
                elevation = if (expanded) 16.dp else 0.dp

            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        modifier = Modifier.paddingFromBaseline(top = 8.dp),
                        text = date,
                        style = MaterialTheme.typography.overline,
                        color = LocalContentColor.current.copy(
                            ContentAlpha.medium
                        )
                    )
                    Text(title, style = MaterialTheme.typography.h6)
                }
            }

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
    onCategoryChange: (CategoryModel) -> Unit
) {
    CategoryTheme(categorySelectionModel.selectedColorCategory) {
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
fun AddBirthdayTopBar() {
    val topBarBackground = MaterialTheme.colors.primary
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        backgroundColor = topBarBackground,
    )
}

fun Locale.toJavaLocale(): java.util.Locale = java.util.Locale.forLanguageTag(this.toLanguageTag())
fun Month.localizedName(locale: Locale): String =
    this.getDisplayName(TextStyle.FULL, locale.toJavaLocale())
