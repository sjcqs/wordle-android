import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    compileOnly(libs.kotlin.gradle)

    compileOnly(libs.android.gradle)
    compileOnly(libs.hilt.gradle)

}

gradlePlugin {
    plugins {
        register("fr.sjcqs.android.app") {
            id = "fr.sjcqs.android.app"
            implementationClass = "fr.sjcqs.AndroidAppPlugin"
        }
        register("fr.sjcqs.android.feature") {
            id = "fr.sjcqs.android.feature"
            implementationClass = "fr.sjcqs.AndroidFeaturePlugin"
        }
        register("fr.sjcqs.android.lib") {
            id = "fr.sjcqs.android.lib"
            implementationClass = "fr.sjcqs.AndroidLibPlugin"
        }
        register("fr.sjcqs.android.wiring.lib") {
            id = "fr.sjcqs.android.wiring.lib"
            implementationClass = "fr.sjcqs.AndroidWiringLibPlugin"
        }
        register("fr.sjcqs.jvm.lib") {
            id = "fr.sjcqs.jvm.lib"
            implementationClass = "fr.sjcqs.JvmLibPlugin"
        }
    }
}