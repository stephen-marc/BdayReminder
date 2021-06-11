package dev.prochnow.bdayreminder.interactor

import dev.prochnow.bdayreminder.di.IoDispatcher
import dev.prochnow.bdayreminder.domain.entity.BirthdayEntity
import dev.prochnow.bdayreminder.domain.entity.CategoryEntity
import dev.prochnow.bdayreminder.domain.store.BirthdayDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

class BirthdateInteractor @Inject constructor(
    private val birthdayDataStore: BirthdayDataStore,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    val birthdays = birthdayDataStore.birthdays

    suspend fun saveEntry(
        uuid: UUID = UUID.randomUUID(),
        name: String,
        date: LocalDate,
        category: String
    ) {
        withContext(ioDispatcher) {
            birthdayDataStore.upsertBirthday(
                BirthdayEntity(
                    uuid,
                    name,
                    date,
                    CategoryEntity.valueOf(category)
                )
            )
        }
    }

    suspend fun deleteEntry(
        uuid: UUID
    ) {
        withContext(ioDispatcher) {
            birthdayDataStore.deleteBirthday(uuid)
        }
    }
}
