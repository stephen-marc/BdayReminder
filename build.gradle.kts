// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
        maven("https://www.jetbrains.com/intellij-repository/releases")
        maven("https://dl.bintray.com/arrow-kt/arrow-kt/")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0-beta03")
        classpath(Kotlin.gradlePlugin)
        classpath(SqlDelight.gradlePlugin)
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.36")
    }
}



tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
