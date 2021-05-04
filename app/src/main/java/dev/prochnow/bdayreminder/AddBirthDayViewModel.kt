package dev.prochnow.bdayreminder

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.Month

val CATEGORIES = listOf(
    CategoryModel("None", Color.Gray),
    CategoryModel("Family", Color.Blue),
    CategoryModel("Friends", Color.Red),
    CategoryModel("Work", Color.Green),
)

class AddBirthDayViewModel(
    private val state: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(AddBirthdayModel())
    val viewState: StateFlow<AddBirthdayModel>
        get() = _state

    init {
        state.get<AddBirthdayModel>("state")?.run { _state.value = this }
    }

    override fun onCleared() {
        super.onCleared()
        state.set("state", _state.value)
    }

    data class AddBirthdayModel(
        val name: String = "",
        val day: Int? = null,
        val month: Month? = null,
        val availableCategories: List<CategoryModel> = CATEGORIES,
        val selectedCategory: CategoryModel = CATEGORIES.first()
    ) {
        val dayString = day?.toString()
    }

    fun updateName(newValue: String) {
        _state.value = _state.value.copy(name = newValue)
    }

    fun updateDay(newValue: Int) {
        _state.value = _state.value.copy(day = newValue)
    }

    fun updateMonth(newValue: Month) {
        _state.value = _state.value.copy(month = newValue)
    }

    fun updateCategory(newValue: CategoryModel) {
        _state.value = _state.value.copy(selectedCategory = newValue)
    }
}


data class CategoryModel(
    val name: String,
    val color: Color
)
