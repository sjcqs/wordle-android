plugins {
    id("fr.sjcqs.android.lib")
    id("kotlin-kapt")
}

apply(plugin = "app.cash.molecule")

dependencies {
    implementation(project(":ui"))
    implementation(project(":tools:molecule-viewmodel"))

    implementation(libs["androidx.lifecycle.viewmodel.ktx"])

    implementation(libs["kotlin.coroutines.core"])
    implementation(libs["kotlin.coroutines.android"])

    implementation(libs["hilt.android"])
    implementation(libs["androidx.hilt.compose"])
    kapt(libs["hilt.compiler"])
    kapt(libs["androidx.hilt.compiler"])

    testImplementation(libs["junit"])
    testImplementation(libs["robolectric"])
    testImplementation(libs["androidx.test.core"])
    testImplementation(libs["androidx.test.rules"])
}