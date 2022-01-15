import gradle.kotlin.dsl.accessors._c4fcb3ec03f9133cd9522b64213c2cea.implementation

plugins {
    id("fr.sjcqs.android.lib")
    id("kotlin-kapt")
}

//apply(plugin = "app.cash.molecule")

android.buildFeatures {
    resValues = true
}

dependencies {
    implementation(project(":ui"))
    //implementation(project(":tools:molecule-viewmodel"))
    implementation(project(":tools:extensions:coroutines"))

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