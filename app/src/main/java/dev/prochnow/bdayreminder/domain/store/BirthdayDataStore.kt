package dev.prochnow.bdayreminder.domain.store

import dev.prochnow.bdayreminder.domain.entity.BirthdayEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

interface BirthdayDataStore {
    val birthdays: Flow<List<BirthdayEntity>>
    suspend fun upsertBirthday(birthdayEntity: BirthdayEntity)
    suspend fun deleteBirthday(uuid: UUID)
}
