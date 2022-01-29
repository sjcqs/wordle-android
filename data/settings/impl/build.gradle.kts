plugins {
    id("fr.sjcqs.jvm.lib")
}

dependencies {
    implementation(libs.androidx.datastore.preferences.core)

    implementation(projects.data.settings.public)
    implementation(projects.tools.annotations)
}