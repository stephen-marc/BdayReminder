package dev.prochnow.bdayreminder.interactor

import dev.prochnow.bdayreminder.di.IoDispatcher
import dev.prochnow.bdayreminder.domain.entity.BirthdayEntity
import dev.prochnow.bdayreminder.domain.entity.CategoryEntity
import dev.prochnow.bdayreminder.domain.store.BirthdayDataStore
import kotlinx.coroutines.CoroutineDispatcher
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

class BirthdateInteractor @Inject constructor(
    private val birthdayDataStore: BirthdayDataStore,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) {
    suspend fun saveEntry(name: String, date: LocalDate, category: String) {
        birthdayDataStore.upsertBirthday(
            BirthdayEntity(
                UUID.randomUUID(),
                name,
                date,
                CategoryEntity.valueOf(category)
            )
        )
    }
}
