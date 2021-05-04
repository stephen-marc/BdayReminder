package dev.prochnow.bdayreminder

import android.content.Context
import androidx.startup.Initializer
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class StartupInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        initKoin(context)
    }

    private fun initKoin(context: Context) {
        startKoin {
            // Koin Android logger
            androidLogger()
            //inject Android context
            androidContext(context)
            // use modules
            modules(AppModules.all)
        }
        Timber.d("Initialized Koin")
    }

    override fun dependencies() = listOf<Class<out Initializer<*>>>(LoggingInitializer::class.java,)
}

class LoggingInitializer :  Initializer<Unit> {
    override fun create(context: Context) {
        Timber.plant(Timber.DebugTree())
        Timber.d("Initialized Timber")
    }

    override fun dependencies() = emptyList<Class<out Initializer<*>>>()
}
