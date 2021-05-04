package dev.prochnow.bdayreminder

import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.EnumColumnAdapter
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dev.prochnow.bdayreminder.domain.entity.CategoryEntity
import dev.prochnow.bdayreminder.domain.store.BirthdayDataStore
import dev.prochnow.bdayreminder.storage.BirthdayDao
import dev.prochnow.bdayreminder.storage.SQLBirthdateDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModules {
    val all : List<Module> = listOf(storage)
}

val storage = module {
    single {
        AndroidSqliteDriver(Database.Schema, androidContext(), "storage.db")
    }

    single {
        Database(get<AndroidSqliteDriver>(), birthdayAdapter = Birthday.Adapter(categoryAdapter =  EnumColumnAdapter()), categoryAdapter = Category.Adapter(categoryAdapter =  EnumColumnAdapter()))
    }

    single<BirthdayDataStore> {
        SQLBirthdateDataStore(birthdaysQueries = get())
    }

    single {
        BirthdayDao(get<Database>().birthdaysQueries)
    }
}
