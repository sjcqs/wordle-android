plugins {
    id("config")
    kotlin("jvm")
}

base {
    archivesName.set(fullName)
}

java {
    sourceCompatibility = config.jvm.javaVersion
    targetCompatibility = config.jvm.javaVersion
}

dependencies {
    compileOnly(libs["javaxInject"])
    implementation(libs["kotlin.stdlib"])
}