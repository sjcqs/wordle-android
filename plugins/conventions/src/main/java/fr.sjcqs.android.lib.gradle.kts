plugins {
    id("config")
    id("com.android.library")
    id("kotlin-android")
}

base {
    archivesName.set(fullName)
}

android {
    compileSdk = config.android.compileSdkVersion

    defaultConfig {
        minSdk = config.android.minSdk
        targetSdk = config.android.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = false
        }
    }


    compileOptions {
        sourceCompatibility = config.jvm.javaVersion
        targetCompatibility = config.jvm.javaVersion

        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = config.jvm.kotlinJvm
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=kotlinx.coroutines.FlowPreview"
        )
    }
}

dependencies {
    implementation(project(":tools:annotations"))

    compileOnly(libs["javaxInject"])

    implementation(libs["kotlin.stdlib"])

    coreLibraryDesugaring(libs["desugarJdk"])
}