package dev.prochnow.bdayreminder

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
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
    val viewState: StateFlow<AddBirthdayModel> = _state

    private val _nameModel = MutableStateFlow(NameModel())

    private val _dayModel = MutableStateFlow(DayModel())

    init {
        state.get<AddBirthdayModel>("state")?.run { _state.value = this }

        viewModelScope.launch {
            _nameModel.combine(_dayModel) { name, day ->
                AddBirthdayModel(name, day)
            }.collect { _state.value = it }
        }
    }

    override fun onCleared() {
        super.onCleared()
        state.set("state", _state.value)
    }


    data class NameModel(override val value: String = "", override val validate: Boolean = false) :
        ValidatorModel(value, validate, validator = ::validatorForName, errorFor = ::errorForName)

    data class DayModel(override val value: String = "", override val validate: Boolean = false) :
        ValidatorModel(value, validate, validator = ::validatorForName, errorFor = ::errorForName)

    data class AddBirthdayModel(
        val name: NameModel = NameModel(),
        val day: DayModel = DayModel(),
        val month: Month? = null,
        val year: String? = null,
        val availableCategories: List<CategoryModel> = CATEGORIES,
        val selectedCategory: CategoryModel = CATEGORIES.first(),
        val validate: Boolean = false,
    )

    fun updateName(newValue: String) {
        _nameModel.value = _nameModel.value.copy(value = newValue)
    }

    fun updateDay(newValue: Int) {
        _dayModel.value = _dayModel.value.copy(value = newValue.toString())
    }

    fun updateMonth(newValue: Month) {
        _state.value = _state.value.copy(month = newValue)
    }

    fun updateCategory(newValue: CategoryModel) {
        _state.value = _state.value.copy(selectedCategory = newValue)
    }

    fun saveEntry() {
        _nameModel.value = _nameModel.value.copy(validate = true)
        _dayModel.value = _dayModel.value.copy(validate = true)
        if (_nameModel.value.isValid) {
            Timber.d("Is valid")
        }
    }
}

data class ValidationError(
    val stringRes: Int
)


data class CategoryModel(
    val name: String,
    val color: Color
)

abstract class ValidatorModel(
    open val value: String,
    open val validate: Boolean,
    private val validator: (String) -> Boolean = { true },
    private val errorFor: (String) -> String = { "" }
) {
    val isValid: Boolean
        get() = validator(value)
    val showError: Boolean
        get() = validate && !isValid

    fun getErrors(): String {
        return if (!isValid) {
            errorFor(value)
        } else {
            ""
        }
    }
}

private fun validatorForName(text: String): Boolean = text.isNotBlank()


private fun errorForName(text: String): String = "Name is required"
