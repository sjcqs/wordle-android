package fr.sjcqs

import extensions.get
import extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply(AndroidLibPlugin::class.java)
                apply("kotlin-kapt")
            }

            dependencies.apply {
                add("implementation", project(":ui"))
                add("implementation", project(":tools:extensions:coroutines"))

                add("implementation", libs["androidx.lifecycle.viewmodel.ktx"])

                add("implementation", libs["kotlin.coroutines.core"])
                add("implementation", libs["kotlin.coroutines.android"])

                add("implementation", libs["hilt.android"])
                add("implementation", libs["androidx.hilt.compose"])
                add("kapt", libs["hilt.compiler"])
                add("kapt", libs["androidx.hilt.compiler"])

                add("testImplementation", libs["junit"])
                add("testImplementation", libs["robolectric"])
                add("testImplementation", libs["androidx.test.core"])
                add("testImplementation", libs["androidx.test.rules"])
            }
        }
    }
}