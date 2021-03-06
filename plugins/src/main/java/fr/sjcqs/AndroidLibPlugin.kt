@file:Suppress("UnstableApiUsage")

package fr.sjcqs

import Config
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import extensions.configureAndroidAndKotlin
import extensions.disableDebugBuildType
import extensions.get
import extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply<ConfigureArchivesNamePlugin>()
                apply<LibraryPlugin>()
                apply("kotlin-android")
            }

            disableDebugBuildType()
            extensions.configure<LibraryExtension> {
                configureAndroidAndKotlin(this)

                defaultConfig.targetSdk = Config.android.targetSdk
                buildTypes {
                    all { isMinifyEnabled = false }
                }

            }
            dependencies {
                add("implementation", project(":tools:annotations"))

                add("compileOnly", libs["javaxInject"])

                add("implementation", libs["kotlin.stdlib"])
            }
        }
    }
}