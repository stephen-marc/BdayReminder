package dev.prochnow.bdayreminder

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.prochnow.bdayreminder.ui.components.ActionDrawer
import dev.prochnow.bdayreminder.ui.components.BirthdateCard
import dev.prochnow.bdayreminder.ui.components.BirthdayInputComponent
import dev.prochnow.bdayreminder.ui.get
import dev.prochnow.bdayreminder.ui.theme.CategoryTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.*

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
            Surface(color = MaterialTheme.colors.background) {
                when (val state = listState) {
                    is BirthdayListViewModel.BirthdateListViewState.ContentState -> BirthdateList(
                        birthdates = state.list,
                        updateBirthdate = birthdayListViewModel::updateBirthdate,
                        deleteBirthdate = birthdayListViewModel::deleteBirthdate
                    )
                    BirthdayListViewModel.BirthdateListViewState.EmptyState -> {
                    }
                    BirthdayListViewModel.BirthdateListViewState.InitialState -> {
                    }
                }
            }

        }
    }
}

@Composable
fun BirthdateList(
    birthdates: List<BirthdayListViewModel.BirthdateModel>,
    updateBirthdate: (BirthdayListViewModel.BirthdateModel) -> Unit,
    deleteBirthdate: (UUID) -> Unit,
) {
    var selectedDrawer by remember { mutableStateOf<UUID?>(null) }

    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(birthdates) { index, item ->
            CategoryTheme(colorPalette = item.categoryModel.selectedColorCategory) {
                BirthdateCard(
                    backContent = {
                        ActionDrawer(item, updateBirthdate, {
                            deleteBirthdate(it)
                            selectedDrawer = null
                        })
                    },
                    topContent = {
                        BirthdateInfo(item = item) {
                            selectedDrawer = if (selectedDrawer != item.uuid) item.uuid else null
                        }
                    },
                    expanded = item.uuid == selectedDrawer,
                )
            }
        }
    }
}

@Composable
private fun BirthdateInfo(item: BirthdayListViewModel.BirthdateModel, onClick: () -> Unit) {
    Column(
        Modifier
            .clickable { onClick() }
            .padding(16.dp)) {
        Text(
            modifier = Modifier.paddingFromBaseline(top = 8.dp),
            text = item.dateModel.date.toString(),
            style = MaterialTheme.typography.overline,
            color = LocalContentColor.current.copy(
                ContentAlpha.medium
            )
        )
        Text(
            item.nameModel.value.get(LocalContext.current),
            style = MaterialTheme.typography.h6
        )
    }
}

@Preview(device = Devices.PIXEL_4)
@Composable
fun PreviewBirthdateCard() {
    CategoryTheme {
        BirthdateCard(backContent = {
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
        }) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    modifier = Modifier.paddingFromBaseline(top = 8.dp),
                    text = "Lorem",
                    style = MaterialTheme.typography.overline,
                    color = LocalContentColor.current.copy(
                        ContentAlpha.medium
                    )
                )
                Text("Lorem", style = MaterialTheme.typography.h6)
            }
        }
    }
}


@Composable
private fun AddBirthdaySheetComponent(
    nameModel: NameModel,
    timeModel: TimeModel,
    categorySelectionModel: CategorySelectionModel,
    onCreateEntryClicked: () -> Unit,
    onNameChange: (String) -> Unit,
    onDateChange: (LocalDate) -> Unit,
    onCategoryChange: (CategoryModel) -> Unit
) {
    CategoryTheme(categorySelectionModel.selectedColorCategory) {
        Column {
            TopAppBar(
                title = { Text(stringResource(id = R.string.add_birthday_screen_title)) },
                elevation = 0.dp, actions = {
                    AppBarActionIcon(onCreateEntryClicked)
                }
            )
            BirthdayInputComponent(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 40.dp, start = 16.dp, end = 16.dp),
                nameModel = nameModel,
                timeModel = timeModel,
                categorySelectionModel = categorySelectionModel,
                onNameChange = onNameChange,
                onDateChange = onDateChange,
                onCategoryChange = onCategoryChange
            )
        }

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

fun Locale.toJavaLocale(): java.util.Locale =
    java.util.Locale.forLanguageTag(this.toLanguageTag())

fun Month.localizedName(locale: Locale): String =
    this.getDisplayName(TextStyle.FULL, locale.toJavaLocale())
