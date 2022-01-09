plugins {
    id("fr.sjcqs.android.wiring.lib")
}

dependencies {
    api(projects.tools.haptics.public)
    api(projects.tools.haptics.impl)
}