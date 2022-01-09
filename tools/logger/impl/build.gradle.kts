plugins {
    id("fr.sjcqs.android.lib")
}

dependencies {
    implementation(projects.tools.logger.public)
    implementation(libs.timber)
}