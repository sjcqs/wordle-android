plugins {
    id("fr.sjcqs.android.wiring.lib")
}

dependencies {
    api(projects.data.settings.public)
    api(projects.data.settings.impl)
}
