plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.squareup.sqldelight")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 30

    defaultConfig {
        applicationId = "dev.prochnow.bdayreminder"
        minSdk = 28
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"
        compileSdk = 30

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Compose.version
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
        freeCompilerArgs = freeCompilerArgs + "-Xallow-jvm-ir-dependencies"
    }
}

dependencies {
    implementation(Kotlin.stdlib)
    implementation(Kotlin.coroutines)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.core)
    implementation(AndroidX.navigationCompose)
    implementation(AndroidX.lifecycleCompose)
    implementation(AndroidX.startup)
    implementation(Libs.material)
    implementation(Libs.timber)
    implementation(Compose.ui)
    implementation(Compose.tooling)
    implementation(Compose.material)
    implementation(Compose.runtime)
    implementation(Compose.runtimeLivedate)
    implementation(Coil.accompanist)
    implementation(Coil.core)
    implementation(SqlDelight.coroutineExt)
    implementation(SqlDelight.androidDriver)

    implementation("com.facebook.stetho:stetho:1.5.1")

    implementation("com.google.dagger:hilt-android:2.35")
    kapt("com.google.dagger:hilt-android-compiler:2.35")

    implementation("androidx.hilt:hilt-navigation-compose:1.0.0-alpha02")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    implementation("io.github.vanpra.compose-material-dialogs:datetime:0.4.1")
    implementation("io.arrow-kt:arrow-optics:0.13.2")
    kapt("io.arrow-kt:arrow-meta:0.13.2")
    testImplementation(Junit.junit4)
    androidTestImplementation(AndroidX.Test.junit)
    androidTestImplementation(AndroidX.Test.espresso)
}
