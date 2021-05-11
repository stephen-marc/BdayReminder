package dev.prochnow.bdayreminder

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.prochnow.bdayreminder.ui.theme.BdayTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getStateViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BirthdayListScreen(
    addBirthDayViewModel: AddBirthDayViewModel = getStateViewModel()
) {
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)


    val coroutineScope = rememberCoroutineScope()
    val state by addBirthDayViewModel.viewState.collectAsState()


    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetBackgroundColor = MaterialTheme.colors.surface,
        sheetContent = {
            Column(
                modifier = Modifier
                    .background(Color.Blue)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    color = Color.Red
                ) {
                    Column() {
                        TopAppBar(
                            backgroundColor = state.selectedCategory.color,
                            contentColor = MaterialTheme.colors.onPrimary,
                            elevation = 0.dp,
                            title = { Text(text = stringResource(id = R.string.app_name)) },
                            actions = {
                                IconButton(onClick = { addBirthDayViewModel.saveEntry() }) {
                                    Image(
                                        imageVector = Icons.Filled.CheckCircle,
                                        contentDescription = stringResource(id = R.string.cd_add_new_birthday),
                                        colorFilter = ColorFilter.tint(LocalContentColor.current)
                                    )
                                }
                            }
                        )
                        BirthdayInformationComponent(addBirthDayViewModel)
                    }


                }


            }
        }) {
        Scaffold(
            topBar = {
                TopBar(onAddBirthdateClick = {
                    coroutineScope.launch {
                        if (bottomSheetState.isVisible) {
                            bottomSheetState.hide()
                        } else {
                            bottomSheetState.show()
                        }
                    }
                })
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    coroutineScope.launch {
                        if (bottomSheetState.isVisible) {
                            bottomSheetState.hide()
                        } else {
                            bottomSheetState.show()
                        }
                    }
                }) {

                }
            },
            floatingActionButtonPosition = FabPosition.End,
            modifier = Modifier.scrollable(rememberScrollState(), Orientation.Vertical),
        ) {

        }
    }


}


@OptIn(ExperimentalMaterialApi::class)
private suspend fun BottomSheetScaffoldState.toggle() {
    if (bottomSheetState.isCollapsed) {
        bottomSheetState.expand()
    } else {
        bottomSheetState.collapse()
    }
}

@Composable
fun TopBar(onAddBirthdateClick: () -> Unit = { /*TODO*/ }) {
    val topBarBackground = MaterialTheme.colors.primary
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        backgroundColor = topBarBackground,
        actions = {
            IconButton(onClick = onAddBirthdateClick) {
                Image(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = stringResource(id = R.string.cd_add_new_birthday),
                    colorFilter = ColorFilter.tint(
                        contentColorFor(backgroundColor = topBarBackground)
                    )
                )
            }
        })
}


@Preview
@Composable
fun PreviewBirthdayListScreen() {
    BdayTheme {
        BirthdayListScreen()
    }
}

