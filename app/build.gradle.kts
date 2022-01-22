import com.android.build.gradle.internal.cxx.configure.createNativeBuildSystemVariantConfig

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

    signingConfigs {
        getByName("debug") {
            storeFile = file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildTypes {
        debug {
            manifestPlaceholders.put("crashlyticsCollectionEnabled", false)
            signingConfig = signingConfigs.getByName("debug")
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