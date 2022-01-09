plugins {
    id("fr.sjcqs.android.lib")
    id("kotlin-kapt")
}

dependencies {
    implementation(libs["hilt.core"])
    kapt(libs["hilt.compiler"])
}