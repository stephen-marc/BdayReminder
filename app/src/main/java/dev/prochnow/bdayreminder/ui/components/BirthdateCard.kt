package dev.prochnow.bdayreminder.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.prochnow.bdayreminder.R
import dev.prochnow.bdayreminder.ui.theme.CategoryTheme


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BirthdateCard(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    backContent: @Composable () -> Unit,
    topContent: @Composable () -> Unit,
) {
    val transition = updateTransition(expanded)

    var backElementOffsetPx by remember { mutableStateOf(0) }
    val elevation by transition.animateDp(label = "elevation") {
        when (it) {
            true -> 8.dp
            false -> 0.dp
        }
    }
    val backElementOffsetDp = with(LocalDensity.current) { backElementOffsetPx.toDp() }

    Layout(
        content = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, MaterialTheme.colors.primary, shape = MaterialTheme.shapes.medium)
                    .heightIn(backElementOffsetDp)
                    .padding(),
            ) {
                AnimatedVisibility(
                    visible = expanded,
                    exit = shrinkVertically(shrinkTowards = Alignment.Top),
                    enter = expandVertically(expandFrom = Alignment.Top)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = backElementOffsetDp)
                    ) {
                        backContent()
                    }
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        backElementOffsetPx = it.size.height / 2
                    },
                backgroundColor = MaterialTheme.colors.primary,
                elevation = elevation,
                content = topContent
            )
        }
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        val backPlaceable = placeables[0]
        val topPlaceable = placeables[1]
        val combinedHeight =
            topPlaceable.height + maxOf(backPlaceable.height - backElementOffsetPx, 0)
        layout(
            constraints.maxWidth,
            combinedHeight
        ) {
            backPlaceable.placeRelative(0, backElementOffsetPx)
            topPlaceable.placeRelative(0, 0)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBirthdateCardExpanded() {
    CategoryTheme {
        BirthdateCard(expanded = true, backContent = {
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

@Preview(showBackground = true)
@Composable
fun PreviewBirthdateCard() {
    CategoryTheme {
        BirthdateCard(expanded = false, backContent = {
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

