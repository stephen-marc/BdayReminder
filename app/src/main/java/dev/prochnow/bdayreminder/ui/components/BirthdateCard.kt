package dev.prochnow.bdayreminder.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

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
                    .heightIn(backElementOffsetDp),
            ) {
//                CompositionLocalProvider(LocalContentColor provides (MaterialTheme.colors.primary)) {
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
//            }
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

