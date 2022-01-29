plugins {
    id("fr.sjcqs.android.wiring.lib")
}

dependencies {
    api(projects.feature.game)
    api(projects.feature.stats)

    api(projects.data.game.wiring)
    api(projects.data.settings.wiring)

    api(projects.tools.annotations)
    api(projects.tools.haptics.wiring)
    api(projects.tools.logger.wiring)
    api(projects.tools.lifecycleLogging.wiring)

    implementation(libs.androidx.hilt.compose)

    kapt(libs.androidx.hilt.compiler)
}