plugins {
    id("fr.sjcqs.android.wiring.lib")
}

dependencies {
    api(projects.data.game.public)
    api(projects.data.game.impl)
    implementation(libs.sqldelight.driver)
}
