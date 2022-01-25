tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = libs.versions.gradle.get()
    doLast {
        delete(jarFile)
        delete(batchScript)
        delete(scriptFile)
        delete(archivePath)
    }
}