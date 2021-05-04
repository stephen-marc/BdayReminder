plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.squareup.sqldelight")
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

    buildFeatures{
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Compose.version
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }
}

dependencies {
    implementation(Kotlin.stdlib)
    implementation(Kotlin.coroutines)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.core)
    implementation(AndroidX.navigationCompose)
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
    implementation(Koin.androidExt)
    implementation(Koin.coreAndroid)
    implementation(Koin.compose)
    testImplementation(Junit.junit4)
    androidTestImplementation(AndroidX.Test.junit)
    androidTestImplementation(AndroidX.Test.espresso)
}
