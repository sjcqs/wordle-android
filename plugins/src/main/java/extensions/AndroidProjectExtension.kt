package extensions

import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Project

internal fun Project.disableDebugBuildType() {
    extensions.configure(AndroidComponentsExtension::class.java) {
        beforeVariants(selector().withBuildType("debug")) { builder ->
            builder.enable = false
        }
    }
}