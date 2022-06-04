plugins {
    id("fr.sjcqs.android.feature")
    id("fr.sjcqs.android.compose.lib")
}

dependencies {
    implementation(projects.data.game.public)
    implementation(projects.data.settings.public)
}