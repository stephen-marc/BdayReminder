package dev.prochnow.bdayreminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.optics
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prochnow.bdayreminder.domain.entity.BirthdayEntity
import dev.prochnow.bdayreminder.interactor.BirthdateInteractor
import dev.prochnow.bdayreminder.ui.LocalizedString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BirthdayListViewModel @Inject constructor(
    private val birthdateInteractor: BirthdateInteractor
) : ViewModel() {
    private val _state =
        MutableStateFlow<BirthdateListViewState>(BirthdateListViewState.InitialState)
    val viewState: StateFlow<BirthdateListViewState> = _state

    init {
        viewModelScope.launch {
            birthdateInteractor.birthdays.collect { list ->
                _state.value = when {
                    list.isNotEmpty() -> BirthdateListViewState.ContentState(
                        list.map(BirthdayEntity::toBirthdateModel)
                    )
                    else -> BirthdateListViewState.EmptyState
                }
            }
        }
    }

    fun updateBirthdate(model: BirthdateModel) {
        viewModelScope.launch {
            birthdateInteractor.saveEntry(
                uuid = model.uuid,
                name = model.nameModel.value.stringValue,
                date = model.dateModel.date!!,
                category = model.categoryModel.selectedColorCategory.name
            )
        }
    }

    fun deleteBirthdate(uuid: UUID) {
        viewModelScope.launch {
            birthdateInteractor.deleteEntry(uuid = uuid)
        }
    }

    sealed class BirthdateListViewState {
        object InitialState : BirthdateListViewState()
        object EmptyState : BirthdateListViewState()
        data class ContentState(
            val list: List<BirthdateModel>
        ) : BirthdateListViewState()
    }

    @optics
    data class BirthdateModel(
        val uuid: UUID,
        val nameModel: NameModel,
        val dateModel: TimeModel,
        val categoryModel: CategorySelectionModel
    ) {
        companion object
    }
}

fun BirthdayEntity.toBirthdateModel(): BirthdayListViewModel.BirthdateModel {
    return BirthdayListViewModel.BirthdateModel(
        uuid = uuid,
        nameModel = NameModel(personName.asRawString(), validate = true),
        dateModel = TimeModel(date, validate = true),
        categoryModel = CategorySelectionModel(
            selectedColorCategory = CategoryModel.valueOf(
                category.name
            )
        )
    )
}


fun String.asRawString(): LocalizedString.RawString = LocalizedString.RawString(this)


