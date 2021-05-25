package dev.prochnow.bdayreminder.domain.store

import dev.prochnow.bdayreminder.domain.entity.BirthdayEntity
import kotlinx.coroutines.flow.Flow

interface BirthdayDataStore {
    val birthdays: Flow<List<BirthdayEntity>>
    suspend fun upsertBirthday(birthdayEntity: BirthdayEntity)
}
