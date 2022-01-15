plugins {
    id("fr.sjcqs.android.lib")
}

android {
    buildFeatures.compose = true
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    api(libs.androidx.navigation.compose)

    implementation(projects.feature.game)
    implementation(projects.tools.logger.compose)
}