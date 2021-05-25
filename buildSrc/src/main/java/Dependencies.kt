object Compose {
    val version = "1.0.0-beta07"

    val ui = "androidx.compose.ui:ui:$version"
    val material = "androidx.compose.material:material:$version"
    val tooling = "androidx.compose.ui:ui-tooling:$version"
    val runtime = "androidx.compose.runtime:runtime:$version"
    val runtimeLivedate = "androidx.compose.runtime:runtime-livedata:$version"
}

object Libs {
    val timber = "com.jakewharton.timber:timber:4.7.1"
    val material = "com.google.android.material:material:1.3.0"
}

object SqlDelight {
    val version = "1.5.0"

    val gradlePlugin = "com.squareup.sqldelight:gradle-plugin:$version"
    val androidDriver = "com.squareup.sqldelight:android-driver:$version"
    val coroutineExt = "com.squareup.sqldelight:coroutines-extensions-jvm:$version"
}


object AndroidX {
    val core = "androidx.core:core-ktx:1.3.2"
    val appCompat = "androidx.appcompat:appcompat:1.2.0"
    val navigationCompose = "androidx.navigation:navigation-compose:2.4.0-alpha01"
    val startup = "androidx.startup:startup-runtime:1.0.0"
    val lifecycleCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha05"

    object Test {
        val junit = "androidx.test.ext:junit:1.1.2"
        val espresso = "androidx.test.espresso:espresso-core:3.3.0"
    }
}

object Coil {
    val core = "io.coil-kt:coil:1.1.1"
    val accompanist = "dev.chrisbanes.accompanist:accompanist-coil:0.6.0"
}

object Kotlin {
    val version = "1.4.32"

    val stdlib = "org.jetbrains.kotlin:kotlin-stdlib"
    val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3"
    val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
}

object Koin {
    val version = "3.0.1"

    val coreAndroid = "io.insert-koin:koin-android:$version"
    val androidExt = "io.insert-koin:koin-android-ext:$version"
    val workmanager = "io.insert-koin:koin-androidx-workmanager:$version"
    val compose = "io.insert-koin:koin-androidx-compose:$version"
}

object Junit {
    val junit4 = "junit:junit:4.+"
}
