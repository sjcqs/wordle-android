plugins {
    id("fr.sjcqs.android.lib")
}

android {
    buildFeatures.compose = true
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
}

dependencies {
    api(projects.tools.haptics.public)
    implementation(libs.compose.foundation.foundation)
}