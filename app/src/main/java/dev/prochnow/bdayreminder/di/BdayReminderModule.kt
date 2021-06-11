package dev.prochnow.bdayreminder.di

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.prochnow.bdayreminder.Birthday
import dev.prochnow.bdayreminder.BirthdaysQueries
import dev.prochnow.bdayreminder.Database
import dev.prochnow.bdayreminder.domain.store.BirthdayDataStore
import dev.prochnow.bdayreminder.storage.LocalDateColumnAdapter
import dev.prochnow.bdayreminder.storage.SqlBirthdateDataStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BdayReminderModule {

    @Binds
    @Singleton
    abstract fun bindBirthdayDataStore(dataStore: SqlBirthdateDataStore): BirthdayDataStore

    companion object {
        @Provides
        @Singleton
        fun provideBirthdaysQueries(database: Database): BirthdaysQueries {
            return database.birthdaysQueries
        }

        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext appContext: Context): Database {
            return Database(
                AndroidSqliteDriver(Database.Schema, appContext, "storage.db"),
                birthdayAdapter = Birthday.Adapter(LocalDateColumnAdapter())
            )
        }
    }
}
