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
    api(libs.androidx.navigation.compose)

    implementation(projects.feature.guessing)
    implementation(projects.tools.logger.compose)
}