package dev.prochnow.bdayreminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prochnow.bdayreminder.domain.EntityAdapter
import dev.prochnow.bdayreminder.domain.entity.BirthdayEntity
import dev.prochnow.bdayreminder.interactor.BirthdateInteractor
import dev.prochnow.bdayreminder.ui.LocalizedString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BirthdayListViewModel @Inject constructor(
    private val birthdateInteractor: BirthdateInteractor,
    private val birthdateMapper: BirthdateMapper
) : ViewModel() {
    private val _state =
        MutableStateFlow<BirthdateListViewState>(BirthdateListViewState.InitialState)
    val viewState: StateFlow<BirthdateListViewState> = _state

    init {
        viewModelScope.launch {
            birthdateInteractor.birthdays.collect { list ->
                _state.value = when {
                    list.isNotEmpty() -> BirthdateListViewState.ContentState(
                        list.map(birthdateMapper::encode)
                    )
                    else -> BirthdateListViewState.EmptyState
                }
            }
        }
    }

    sealed class BirthdateListViewState {
        object InitialState : BirthdateListViewState()
        object EmptyState : BirthdateListViewState()
        data class ContentState(
            val list: List<BirthdateModel>
        ) : BirthdateListViewState()
    }

    data class BirthdateModel(
        val uuid: UUID,
        val title: LocalizedString,
        val date: LocalizedString,
        val category: CategoryModel
    )
}

class BirthdateMapper @Inject constructor() :
    EntityAdapter<BirthdayEntity, BirthdayListViewModel.BirthdateModel> {

    override fun decode(data: BirthdayListViewModel.BirthdateModel): BirthdayEntity {
        TODO()
    }

    override fun encode(entity: BirthdayEntity): BirthdayListViewModel.BirthdateModel {
        return BirthdayListViewModel.BirthdateModel(
            entity.uuid,
            LocalizedString.resource(
                R.string.birthdate_title, entity.personName, LocalDate.now().year - entity.date.year
            ),
            LocalizedString.raw(
                entity.date.format(
                    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                )
            ),
            CategoryModel.valueOf(entity.category.name)
        )
    }

}
