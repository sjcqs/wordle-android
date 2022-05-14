package fr.sjcqs

import extensions.get
import extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidWiringLibPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply(AndroidLibPlugin::class.java)
                apply("kotlin-kapt")
            }

            dependencies.apply {
                add("implementation", libs["hilt.core"])
                add("kapt", libs["hilt.compiler"])
            }
        }
    }

}