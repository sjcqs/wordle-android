plugins {
    id("fr.sjcqs.android.lib")
}

dependencies {
    compileOnly(libs.molecule.runtime)
    compileOnly(libs.androidx.lifecycle.viewmodel.ktx)
    compileOnly(libs.kotlin.coroutines.core)
}