plugins {
    id("fr.sjcqs.android.wiring.lib")
}

dependencies {
    implementation(libs.androidx.datastore.preferences.android)
    api(projects.data.settings.public)
    api(projects.data.settings.impl)
    api(projects.data.settings.android)
}
