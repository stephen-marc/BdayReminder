// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://www.jetbrains.com/intellij-repository/releases")
        maven("https://jetbrains.bintray.com/intellij-third-party-dependencies" )
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0-alpha15")
        classpath(Kotlin.gradlePlugin)
        classpath(SqlDelight.gradlePlugin)
    }
}



tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
