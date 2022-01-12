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
    api(libs.androidx.core)
    api(libs.google.material)

    api(libs.compose.foundation.foundation)
    api(libs.compose.foundation.layout)
    api(libs.compose.material.material)
    api(libs.compose.animation.animation)
    api(libs.compose.ui.ui)
    api(libs.compose.ui.util)
    api(libs.compose.ui.viewbinding)

    api(libs.accompanist.insets)
    api(libs.accompanist.insetsui)
    api(libs.accompanist.systemuicontroller)
    api(libs.accompanist.pager)

    debugApi(libs.compose.ui.preview)
    debugApi(libs.compose.ui.tooling)

    api(projects.tools.logger.compose)
    api(projects.tools.haptics.compose)
}