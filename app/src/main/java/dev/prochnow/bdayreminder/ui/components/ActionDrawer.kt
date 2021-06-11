package dev.prochnow.bdayreminder.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.prochnow.bdayreminder.*
import dev.prochnow.bdayreminder.R
import dev.prochnow.bdayreminder.ui.get
import dev.prochnow.bdayreminder.ui.theme.BdayTheme
import java.util.*


enum class ActionDrawerState {
    DEFAULT, EDIT, DELETE
}

@Composable
fun ActionDrawer(
    birthdateModel: BirthdayListViewModel.BirthdateModel,
    onSave: (BirthdayListViewModel.BirthdateModel) -> Unit,
    onDelete: (UUID) -> Unit
) {
    var state by remember {
        mutableStateOf(ActionDrawerState.DEFAULT)
    }
    val resetDrawer = {
        state = ActionDrawerState.DEFAULT
    }

    Box(modifier = Modifier.animateContentSize()) {
        when (state) {
            ActionDrawerState.DEFAULT -> ActionDrawerDefault(
                onEditClick = { state = ActionDrawerState.EDIT },
                onDeleteClick = { state = ActionDrawerState.DELETE }
            )
            ActionDrawerState.EDIT -> ActionDrawerEdit(
                birthdateModel = birthdateModel,
                onCancel = resetDrawer
            ) { model ->
                onSave(model)
                resetDrawer()
            }
            ActionDrawerState.DELETE -> ActionDrawerDelete(
                uuid = birthdateModel.uuid,
                name = birthdateModel.nameModel.value.get(LocalContext.current),
                onDelete = onDelete,
                onCancel = resetDrawer
            )
        }
    }
}

@Composable
private fun ActionDrawerDelete(
    uuid: UUID,
    name: String,
    onDelete: (UUID) -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            color = MaterialTheme.colors.onSurface,
            textAlign = TextAlign.Center,
            text = buildAnnotatedString {
                append("Are you sure that you want to delete ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(name)
                }
                append("'s birthday?")
            })
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.End
            ),

            ) {
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
            Button(
                onClick = { onDelete(uuid) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.error,
                    contentColor = MaterialTheme.colors.onError
                )
            ) {
                Text("Delete")
            }
        }
    }
}

@Composable
private fun ActionDrawerEdit(
    birthdateModel: BirthdayListViewModel.BirthdateModel,
    onCancel: () -> Unit,
    onSave: (BirthdayListViewModel.BirthdateModel) -> Unit,
) {
    var nameModel by remember {
        mutableStateOf(birthdateModel.nameModel)
    }
    var dateModel by remember {
        mutableStateOf(birthdateModel.dateModel)
    }
    var categoryModel by remember {
        mutableStateOf(birthdateModel.categoryModel)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        BirthdayInputComponent(
            modifier = Modifier
                .padding(bottom = 16.dp),
            nameModel = nameModel,
            timeModel = dateModel,
            categorySelectionModel = categoryModel,
            onNameChange = {
                nameModel = NameModel.value.set(
                    nameModel,
                    it.asRawString()
                )
            },
            onDateChange = { dateModel = TimeModel.date.set(dateModel, it) },
            onCategoryChange = {
                categoryModel = CategorySelectionModel.selectedColorCategory.set(
                    categoryModel,
                    it
                )
            })
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.End
            )
        ) {
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
            TextButton(
                onClick = {
                    onSave(
                        BirthdayListViewModel.BirthdateModel(
                            birthdateModel.uuid,
                            nameModel,
                            dateModel,
                            categoryModel
                        )
                    )
                },
            ) {
                Text("Save")
            }
        }
    }
}

@Composable
private fun ActionDrawerDefault(onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 2.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = onEditClick) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = stringResource(id = R.string.cd_add_new_birthday),
                tint = MaterialTheme.colors.primary
            )
        }
        IconButton(onClick = onDeleteClick) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = stringResource(id = R.string.cd_add_new_birthday),
                tint = MaterialTheme.colors.error
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewActionDrawerDelete() {
    BdayTheme {
        ActionDrawerDelete(
            uuid = UUID.randomUUID(),
            name = "Lorem Ipsum",
            onCancel = {/*TODO*/ },
            onDelete = {/*TODO*/ })
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewActionDrawerDefault() {
    BdayTheme {
        ActionDrawerDefault(
            onEditClick = {/*TODO*/ },
            onDeleteClick = {/*TODO*/ }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewActionDrawerEdit() {
    BdayTheme {
        ActionDrawerEdit(
            birthdateModel = BirthdayListViewModel.BirthdateModel(
                UUID.randomUUID(),
                NameModel(),
                TimeModel(),
                CategorySelectionModel()
            ),
            onCancel = {/*TODO*/ },
            onSave = {/*TODO*/ }
        )
    }
}

