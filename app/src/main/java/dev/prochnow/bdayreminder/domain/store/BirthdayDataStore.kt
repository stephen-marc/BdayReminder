package dev.prochnow.bdayreminder.domain.store

import dev.prochnow.bdayreminder.domain.entity.BirthdayEntity
import kotlinx.coroutines.flow.Flow

interface BirthdayDataStore {
    abstract val birthdays : Flow<List<BirthdayEntity>>
}
