package dev.prochnow.bdayreminder.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.offset
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.prochnow.bdayreminder.ui.theme.BdayTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MovableFloatingActionButton(
    onClick: () -> Unit,
    yOffsetFraction: Float,
    content: @Composable () -> Unit
) {
    FloatingActionButton(
        modifier = Modifier.offset(0.dp, 100.dp * yOffsetFraction),
        onClick = onClick,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewMovableFloatingActionButton() {
    BdayTheme {
        MovableFloatingActionButton(onClick = { /*TODO*/ }, yOffsetFraction = 0f) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add item")
        }
    }
}
