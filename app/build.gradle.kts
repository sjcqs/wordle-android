plugins {
    id("fr.sjcqs.android.app")
    id("dagger.hilt.android.plugin")
}

android.composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.compose.get()
}

dependencies {
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