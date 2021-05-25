package dev.prochnow.bdayreminder

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.prochnow.bdayreminder.domain.store.BirthdayDataStore
import dev.prochnow.bdayreminder.storage.SqlBirthdateDataStore

@Module
@InstallIn(SingletonComponent::class)
abstract class BdayReminderModule {

    @Binds
    abstract fun bindBirthdayDataStore(dataStore: SqlBirthdateDataStore): BirthdayDataStore

    companion object {
        @Provides
        fun provideBirthdaysQueries(database: Database): BirthdaysQueries {
            return database.birthdaysQueries
        }

        @Provides
        fun provideDatabase(@ApplicationContext appContext: Context): Database {
            return Database(
                AndroidSqliteDriver(Database.Schema, appContext, "storage.db"),
                birthdayAdapter = Birthday.Adapter(LocalDateColumnAdapter())
            )
        }
    }

}
