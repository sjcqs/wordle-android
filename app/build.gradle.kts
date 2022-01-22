import com.android.build.gradle.internal.cxx.configure.createNativeBuildSystemVariantConfig

plugins {
    id("fr.sjcqs.android.app")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
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
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

dependencies {
    implementation(platform(libs.firebase.bom))
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