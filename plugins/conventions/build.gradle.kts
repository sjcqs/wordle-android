plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.kotlin.gradle)
    runtimeOnly(libs.kotlin.gradle)

    implementation(libs.android.gradle)
    implementation(libs.hilt.gradle)

    implementation(projects.config)
}