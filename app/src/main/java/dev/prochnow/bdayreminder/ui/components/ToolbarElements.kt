package dev.prochnow.bdayreminder.ui.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.prochnow.bdayreminder.R
import dev.prochnow.bdayreminder.ui.theme.BdayTheme


@Composable
fun AppBarActionIcon(onCreateEntryClicked: () -> Unit) {
    IconButton(onClick = onCreateEntryClicked) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(id = R.string.cd_navigate_up),
            tint = LocalContentColor.current
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAppBarIcon() {
    BdayTheme {
        AppBarActionIcon(onCreateEntryClicked = {/*TODO*/ })
    }
}

