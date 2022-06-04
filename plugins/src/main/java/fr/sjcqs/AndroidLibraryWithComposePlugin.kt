@file:Suppress("UnstableApiUsage")

package fr.sjcqs

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import extensions.libs
import extensions.requireVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryWithComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(LibraryPlugin::class.java)

            extensions.configure<LibraryExtension> {
                buildFeatures.compose = true
                composeOptions {
                    kotlinCompilerExtensionVersion = libs.requireVersion("composeCompiler")
                }
            }
        }
    }
}