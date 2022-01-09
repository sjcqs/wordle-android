plugins {
    id("fr.sjcqs.android.wiring.lib")
}

dependencies {
    api(projects.feature.guessing)

    api(projects.tools.annotations)
    api(projects.tools.haptics.wiring)
    api(projects.tools.logger.wiring)
    api(projects.tools.lifecycleLogging.wiring)

    implementation(libs.androidx.hilt.compose)

    kapt(libs.androidx.hilt.compiler)
}