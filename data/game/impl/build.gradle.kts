plugins {
    id("fr.sjcqs.android.lib")
    id("com.squareup.sqldelight")
}

dependencies {
    implementation(projects.data.game.public)
    implementation(libs.sqldelight.driver)
    implementation(libs.sqldelight.coroutines)
    implementation(platform(libs.firebase.bom))
    implementation(libs.kotlin.coroutines.playServices)
    implementation(libs.firebase.database)
}