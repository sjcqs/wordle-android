plugins {
    id("config")
    kotlin("jvm")
}

base {
    archivesName.set(fullName)
}

tasks.named<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>("compileKotlin") {
    kotlinOptions {
        jvmTarget = config.jvm.kotlinJvm
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=kotlinx.coroutines.FlowPreview",
            "-Xopt-in=kotlin.RequiresOptIn"
        )
    }
}

java {
    sourceCompatibility = config.jvm.javaVersion
    targetCompatibility = config.jvm.javaVersion
}

dependencies {
    compileOnly(libs["javaxInject"])
    implementation(libs["kotlin.stdlib"])
}