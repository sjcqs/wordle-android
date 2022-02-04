plugins {
    id("fr.sjcqs.android.app")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    defaultConfig {
        versionCode = 18
        versionName = "0.10"
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }

        register("release") {
            storeFile = file("upload.keystore")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = "upload"
            keyPassword = System.getenv("KEYSTORE_PASSWORD")
        }
    }

    buildTypes {
        debug {
            manifestPlaceholders["crashlyticsCollectionEnabled"] = false
            signingConfig = signingConfigs.getByName("debug")
            versionNameSuffix = "-dev"
        }
        release {
            manifestPlaceholders["crashlyticsCollectionEnabled"] = true
        }
    }
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.appcheck)
    implementation(libs.firebase.database)

    implementation(projects.data.settings.android)
    implementation(projects.navigation)
    implementation(projects.ui)
    implementation(projects.wiring)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.splashscreen)

    implementation(libs.kotlin.stdlib)

    implementation(libs.hilt.android)

    debugImplementation(libs.leakCanary)

    kapt(libs.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)
}