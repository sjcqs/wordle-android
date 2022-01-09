plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

dependencies {
    compileOnly(kotlin("gradle-plugin"))
}

gradlePlugin {
    // Register our plugin
    plugins.register("config") {
        id = "config"
        implementationClass = "ConfigLoaderPlugin"
    }
}