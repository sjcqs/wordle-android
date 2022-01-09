plugins {
    id("fr.sjcqs.android.feature")
}

android {
    buildFeatures.compose = true
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
}

dependencies {
    //implementation(projects.data.session.api)
}