package dev.prochnow.bdayreminder

import android.content.Context
import androidx.startup.Initializer
import timber.log.Timber

class StartupInitializer : Initializer<Unit> {
    override fun create(context: Context) {
    }
    
    override fun dependencies() = listOf<Class<out Initializer<*>>>(LoggingInitializer::class.java)
}

class LoggingInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Timber.plant(Timber.DebugTree())
        Timber.d("Initialized Timber")
    }

    override fun dependencies() = emptyList<Class<out Initializer<*>>>()
}
