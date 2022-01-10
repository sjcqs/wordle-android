plugins {
    id("config")
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdk = config.android.compileSdkVersion

    defaultConfig {
        applicationId = config.android.applicationId
        minSdk = config.android.minSdk
        targetSdk = config.android.targetSdk
        versionCode = config.version.code
        versionName = config.version.name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildFeatures {
        compose = true
        resValues = true
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }

        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = config.jvm.javaVersion
        targetCompatibility = config.jvm.javaVersion

        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = config.jvm.kotlinJvm
    }

    kapt {
        correctErrorTypes = true
        useBuildCache = true
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    coreLibraryDesugaring(libs["desugarJdk"])
}