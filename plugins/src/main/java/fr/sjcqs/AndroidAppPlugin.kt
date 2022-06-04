@file:Suppress("UnstableApiUsage")

package fr.sjcqs

import Config
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import extensions.configureAndroidAndKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidAppPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply<AppPlugin>()
                apply("kotlin-android")
            }

            extensions.configure<BaseAppModuleExtension> {
                configureAndroidAndKotlin(this)
                defaultConfig.targetSdk = Config.android.targetSdk

                buildFeatures.buildConfig = true
            }
        }

    }
}


