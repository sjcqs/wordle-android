enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Wordle"

pluginManagement {
    includeBuild("plugins")
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

include(":app")

include(":data:game:public")
include(":data:game:impl")
include(":data:game:wiring")
include(":data:settings:public")
include(":data:settings:impl")
include(":data:settings:wiring")

include(":feature:game")
include(":feature:stats")
include(":feature:settings")

include(":navigation")

include(":tools:annotations")
include(":tools:extensions:coroutines")
include(":tools:haptics:compose")
include(":tools:haptics:public")
include(":tools:haptics:impl")
include(":tools:haptics:wiring")
include(":tools:logger:compose")
include(":tools:logger:public")
include(":tools:logger:impl")
include(":tools:logger:wiring")
include(":tools:lifecycle-logging:public")
include(":tools:lifecycle-logging:wiring")
//include(":tools:molecule-viewmodel")

include(":ui")
include(":wiring")
