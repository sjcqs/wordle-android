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
        versionCode = 3
        versionName = "0.2"
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
            manifestPlaceholders.put("crashlyticsCollectionEnabled", false)
            signingConfig = signingConfigs.getByName("debug")
            versionNameSuffix = "-dev"
        }
        release {
            manifestPlaceholders.put("crashlyticsCollectionEnabled", true)
        }
    }
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.appcheck)
    implementation(libs.firebase.database)

    implementation(projects.navigation)
    implementation(projects.ui)
    implementation(projects.wiring)

    implementation(libs.androidx.activity.compose)

    implementation(libs.kotlin.stdlib)

    implementation(libs.hilt.android)

    debugImplementation(libs.leakCanary)

    kapt(libs.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)
}