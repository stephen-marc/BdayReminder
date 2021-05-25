package dev.prochnow.bdayreminder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.optics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

val CATEGORIES = listOf(
    ColorCategoryModel(LocalizedString.RawString("None"), CategoryModel.NONE),
    ColorCategoryModel(LocalizedString.RawString("Family"), CategoryModel.FAMILY),
    ColorCategoryModel(LocalizedString.RawString("Friends"), CategoryModel.FRIENDS),
    ColorCategoryModel(LocalizedString.RawString("Work"), CategoryModel.WORK),
)

class AddBirthDayViewModel(
    private val state: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(AddBirthdayModel())
    val viewState: StateFlow<AddBirthdayModel> = _state

    private val _nameModel = MutableStateFlow(NameModel())

    private val _timeModel = MutableStateFlow(TimeModel())

    private val _categoryModel = MutableStateFlow(CategorySelectionModel())

    init {
        viewModelScope.launch {
            combine(_nameModel, _timeModel, _categoryModel, ::AddBirthdayModel).collect {
                _state.value = it
            }
        }
    }

    @optics
    data class NameModel(
        override val value: LocalizedString.RawString = LocalizedString.RawString(""),
        override val validate: Boolean = false
    ) : Validatable<LocalizedString> {
        override val isValid: Boolean
            get() = value.stringValue.isNotEmpty()

        override val errors: LocalizedString
            get() = when {
                value.stringValue.isEmpty() -> LocalizedString.resource(R.string.error_required)
                else -> LocalizedString.empty()
            }

        companion object
    }


    data class AddBirthdayModel(
        val name: NameModel = NameModel(),
        val time: TimeModel = TimeModel(),
        val category: CategorySelectionModel = CategorySelectionModel()
    )

    @optics
    data class CategorySelectionModel(
        val availableCategories: List<ColorCategoryModel> = CATEGORIES,
        val selectedColorCategory: ColorCategoryModel = CATEGORIES.first(),
    ) {
        companion object
    }

    @optics
    data class TimeModel(
        val date: LocalDate? = null,
        override val validate: Boolean = false
    ) : Validatable<LocalizedString> {
        override val isValid: Boolean
            get() = date?.isAfter(LocalDate.now())?.not() ?: false
        override val errors: LocalizedString
            get() = when {
                date == null -> LocalizedString.resource(R.string.error_required)
                date.isAfter(LocalDate.now()) -> LocalizedString.resource(R.string.error_future_birthdate)
                else -> LocalizedString.empty()
            }
        override val value: LocalizedString
            get() = LocalizedString.RawString(
                date?.format(
                    DateTimeFormatter.ofLocalizedDate(
                        FormatStyle.MEDIUM
                    )
                ) ?: ""
            )

        companion object
    }

    fun updateName(newValue: String) {
        _nameModel.value =
            NameModel.value.set(_nameModel.value, LocalizedString.RawString(newValue))
    }

    fun updateDate(newValue: LocalDate) {
        _timeModel.update { TimeModel.date.set(this, newValue) }
    }

    fun updateCategory(newValue: ColorCategoryModel) {
        _categoryModel.update { CategorySelectionModel.selectedColorCategory.set(this, newValue) }
    }

    fun saveEntry() {
        _nameModel.update {
            NameModel.validate.set(this, true)
        }
        _timeModel.update {
            TimeModel.validate.set(this, true)
        }
//        _timeModel.update {
////            TimeModel.day.validate.set(
////                this, true
////            ).run {
////                TimeModel.month.validate.set(this, true)
////            }
//        }
    }
}

enum class CategoryModel {
    NONE, FAMILY, FRIENDS, WORK
}


data class ColorCategoryModel(
    val name: LocalizedString,
    val categoryModel: CategoryModel
)

interface Validatable<T> {
    val value: T
    val isValid: Boolean
    val isError: Boolean
        get() = !isValid && validate
    val validate: Boolean
    val errors: LocalizedString
}

fun <T> MutableStateFlow<T>.update(update: T.() -> T) {
    this.value = this.value.update()
}
