package dev.prochnow.bdayreminder

import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.prochnow.bdayreminder.ui.theme.BdayTheme
import org.koin.androidx.compose.getViewModel

@Composable
fun BirthdayListScreen(birthdayListViewModel: BirthdayListViewModel = getViewModel()) {
    Scaffold(topBar = { TopBar() }) {
        LazyColumn() {

        }
    }
}

@Composable
fun BirthdayListScreen() {

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
        BirthdayListScreen(BirthdayListViewModel())
    }
}

