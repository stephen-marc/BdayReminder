package dev.prochnow.bdayreminder.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.prochnow.bdayreminder.CategoryModel
import dev.prochnow.bdayreminder.CategorySelectionModel
import dev.prochnow.bdayreminder.R
import dev.prochnow.bdayreminder.ui.get
import dev.prochnow.bdayreminder.ui.theme.CategoryTheme
import kotlinx.coroutines.flow.collect

@Composable
fun CategorySelectionDropDown(
    modifier: Modifier = Modifier,
    categoryModel: CategorySelectionModel,
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

    val palette = CategoryTheme.colors.categoryPalette
    val fallbackColor = MaterialTheme.colors.primary

    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier,
            value = categoryModel.selectedColorCategory.localizedName.get(LocalContext.current),
            leadingIcon = {
                Canvas(modifier = Modifier
                    .size(16.dp), onDraw = {
                    drawCircle(
                        color = palette[categoryModel.selectedColorCategory]
                            ?: fallbackColor
                    )
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
            categoryModel.availableCategories.forEach {
                DropdownMenuItem(onClick = {
                    onCategoryClick(it)
                    expanded = false
                }) {
                    Canvas(modifier = Modifier
                        .padding(end = 8.dp)
                        .size(16.dp), onDraw = {
                        drawCircle(color = palette[it] ?: fallbackColor)
                    })
                    Text(text = it.localizedName.get(LocalContext.current))
                }
            }
        }
    }
}
