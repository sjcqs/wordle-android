plugins {
    id("fr.sjcqs.android.lib")
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.datastore.preferences.android)
    implementation(projects.data.settings.public)
    implementation(projects.data.settings.impl)

}
