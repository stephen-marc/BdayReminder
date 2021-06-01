package dev.prochnow.bdayreminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.optics
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prochnow.bdayreminder.interactor.BirthdateInteractor
import dev.prochnow.bdayreminder.ui.LocalizedString
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.inject.Inject


@HiltViewModel
class AddBirthDayViewModel @Inject constructor(
    private val birthdateInteractor: BirthdateInteractor,
) : ViewModel() {

    private val _state = MutableStateFlow(AddBirthdayModel())
    val viewState: StateFlow<AddBirthdayModel> = _state

    private val _eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = _eventChannel.receiveAsFlow()

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


    fun updateName(newValue: String) {
        _nameModel.value =
            NameModel.value.set(_nameModel.value, LocalizedString.RawString(newValue))
    }

    fun updateDate(newValue: LocalDate) {
        _timeModel.update { TimeModel.date.set(this, newValue) }
    }

    fun updateCategory(newValue: CategoryModel) {
        _categoryModel.update { CategorySelectionModel.selectedColorCategory.set(this, newValue) }
    }

    fun saveEntry() {
        _nameModel.update {
            NameModel.validate.set(this, true)
        }
        _timeModel.update {
            TimeModel.validate.set(this, true)
        }
        if (entryIsValid()) {
            viewModelScope.launch {
                val date = requireNotNull(_timeModel.value.date)
                birthdateInteractor.saveEntry(
                    name = _nameModel.value.value.stringValue,
                    date = date,
                    category = _categoryModel.value.selectedColorCategory.name
                )
                _eventChannel.send(Event.EntryCreated)
                _eventChannel.send(Event.ShowSnackBar(LocalizedString.ResourceString(R.string.entry_created)))
            }
        }
    }

    private fun entryIsValid(): Boolean {
        return _nameModel.value.isValid && _timeModel.value.isValid
    }

    fun resetEntry() {
        _timeModel.value = TimeModel()
        _nameModel.value = NameModel()
        _categoryModel.value = CategorySelectionModel()
    }

    data class AddBirthdayModel(
        val name: NameModel = NameModel(),
        val time: TimeModel = TimeModel(),
        val category: CategorySelectionModel = CategorySelectionModel()
    )

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

    @optics
    data class CategorySelectionModel(
        val availableCategories: List<CategoryModel> = CategoryModel.all(),
        val selectedColorCategory: CategoryModel = CategoryModel.all().first(),
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


    sealed class Event {
        object EntryCreated : Event()
        data class ShowSnackBar(val message: LocalizedString) : Event()
    }
}

enum class CategoryModel(val localizedName: LocalizedString) {
    NONE(
        LocalizedString.RawString("None")
    ),
    FAMILY(
        LocalizedString.RawString("None")
    ),
    FRIENDS(
        LocalizedString.RawString("None")
    ),
    WORK(LocalizedString.RawString("None"));

    companion object {
        fun all(): List<CategoryModel> {
            return values().asList()
        }
    }
}

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
