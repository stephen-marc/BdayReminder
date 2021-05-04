package dev.prochnow.bdayreminder.storage

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import dev.prochnow.bdayreminder.BirthdaysQueries
import dev.prochnow.bdayreminder.domain.entity.BirthdayEntity
import dev.prochnow.bdayreminder.domain.entity.CategoryEntity
import dev.prochnow.bdayreminder.domain.store.BirthdayDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import java.time.Month
import java.time.Year
import java.util.*

class SQLBirthdateDataStore(private val birthdaysQueries: BirthdaysQueries) : BirthdayDataStore {
    override val birthdays: Flow<List<BirthdayEntity>>
        get() = birthdaysQueries.selectAll { uuid, personName, birthDay, birthMonth, birthYear, category ->
            BirthdayEntity(UUID.fromString(uuid),personName, birthDay, Month.of(birthMonth), birthYear?.run(
                Year::of), category)
        }.asFlow().mapToList()
}
