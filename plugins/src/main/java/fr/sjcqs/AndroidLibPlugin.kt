package fr.sjcqs

import Config
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import extensions.disableDebugBuildType
import extensions.get
import extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AndroidLibPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply(ConfigureArchivesNamePlugin::class.java)
                apply(LibraryPlugin::class.java)
                apply("kotlin-android")
            }

            disableDebugBuildType()
            extensions.configure(LibraryExtension::class.java) {
                compileSdk = Config.android.compileSdkVersion
                defaultConfig.apply {
                    minSdk = Config.android.minSdk
                    targetSdk = Config.android.targetSdk

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
                buildTypes.apply {
                    get("debug").isMinifyEnabled = false
                    get("release").isMinifyEnabled = false
                }

                compileOptions.apply {
                    sourceCompatibility = Config.jvm.javaVersion
                    targetCompatibility = Config.jvm.javaVersion

                    isCoreLibraryDesugaringEnabled = true
                }

                tasks.withType(KotlinCompile::class.java) {
                    kotlinOptions {
                        jvmTarget = Config.jvm.kotlinJvm
                        freeCompilerArgs = freeCompilerArgs + listOf(
                            "-Xopt-in=kotlin.RequiresOptIn",
                            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                            "-Xopt-in=kotlinx.coroutines.FlowPreview"
                        )
                    }
                }

                dependencies.apply {
                    add("implementation", project(":tools:annotations"))

                    add("compileOnly", libs["javaxInject"])

                    add("implementation", libs["kotlin.stdlib"])

                    add("coreLibraryDesugaring", libs["desugarJdk"])
                }
            }
        }
    }
}