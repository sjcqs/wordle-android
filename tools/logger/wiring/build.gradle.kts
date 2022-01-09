plugins {
    id("fr.sjcqs.android.wiring.lib")
}

dependencies {
    api(projects.tools.logger.public)
    api(projects.tools.logger.impl)
}