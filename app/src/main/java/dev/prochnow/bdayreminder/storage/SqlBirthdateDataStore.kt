package dev.prochnow.bdayreminder.storage

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.runtime.coroutines.asFlow
import dev.prochnow.bdayreminder.Birthday
import dev.prochnow.bdayreminder.BirthdaysQueries
import dev.prochnow.bdayreminder.domain.EntityAdapter
import dev.prochnow.bdayreminder.domain.entity.BirthdayEntity
import dev.prochnow.bdayreminder.domain.entity.CategoryEntity
import dev.prochnow.bdayreminder.domain.store.BirthdayDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SqlBirthdateDataStore @Inject constructor(
    private val birthdaysQueries: BirthdaysQueries,
    private val birthdayMapper: BirthdayMapper
) : BirthdayDataStore {
    override val birthdays: Flow<List<BirthdayEntity>>
        get() = birthdaysQueries.selectAll().asFlow().mapToList(mapper = birthdayMapper::decode)

    override suspend fun upsertBirthday(birthdayEntity: BirthdayEntity) {
        birthdaysQueries.insertFullBirthdateObject(birthdayMapper.encode(birthdayEntity))
    }
}

class BirthdayMapper @Inject constructor() : EntityAdapter<BirthdayEntity, Birthday> {
    override fun decode(data: Birthday): BirthdayEntity {
        return BirthdayEntity(
            UUID.fromString(data.uuid),
            data.personName,
            data.date,
            CategoryEntity.valueOf(data.category)
        )
    }

    override fun encode(entity: BirthdayEntity): Birthday {
        return Birthday(
            uuid = entity.uuid.toString(),
            personName = entity.personName,
            date = entity.date,
            category = entity.category.name
        )
    }
}

@JvmOverloads
fun <T : Any, R : Any> Flow<Query<T>>.mapToList(
    context: CoroutineContext = Dispatchers.Default,
    mapper: (T) -> R
): Flow<List<R>> = map {
    withContext(context) {
        it.executeAsList()
    }
}.map {
    withContext(context) {
        it.map(mapper)
    }
}
