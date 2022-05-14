package fr.sjcqs

import Config
import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import extensions.get
import extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AndroidAppPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply(AppPlugin::class.java)
                apply("kotlin-android")
                apply("kotlin-kapt")
            }

            extensions.configure(AppExtension::class.java) {
                compileSdkVersion(Config.android.compileSdkVersion)

                defaultConfig {
                    applicationId = Config.android.applicationId
                    minSdk = Config.android.minSdk
                    targetSdk = Config.android.targetSdk

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                buildFeatures.apply {
                    compose = true
                    buildConfig = true
                }

                buildTypes {
                    getByName("debug") {
                        isMinifyEnabled = false
                        matchingFallbacks.add("release")
                    }

                    getByName("release") {
                        isMinifyEnabled = true
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }

                compileOptions {
                    sourceCompatibility = Config.jvm.javaVersion
                    targetCompatibility = Config.jvm.javaVersion

                    isCoreLibraryDesugaringEnabled = true
                }

                tasks.withType(KotlinCompile::class.java) {
                    kotlinOptions {
                        jvmTarget = Config.jvm.kotlinJvm
                    }
                }

                extensions.configure(KaptExtension::class.java) {
                    correctErrorTypes = true
                    useBuildCache = true
                }
                packagingOptions.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }

            dependencies.apply {
                add("coreLibraryDesugaring", libs["desugarJdk"])
            }
        }

    }
}


