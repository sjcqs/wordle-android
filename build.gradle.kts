// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.android.gradle)
        classpath(libs.hilt.gradle)
        classpath(libs.kotlin.gradle)
        classpath(libs.sqldelight.gradle)
        classpath(libs.google.services)
        classpath(libs.firebase.crashlytics.gradle)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

subprojects {
    apply(plugin = "org.gradle.base")
    project.extensions.configure(BasePluginExtension::class) {
        archivesName.set(project.fullName)
    }
}

val Project.fullName: String
    get() = mutableListOf(name).apply {
        var project: Project = this@fullName
        while (project.parent != null) {
            val parent = project.parent
            project = parent ?: break
            add(parent.name)
        }
    }.joinToString("-")

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "7.3.3"
}