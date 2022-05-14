import org.gradle.api.JavaVersion

object Config {
    val android = AndroidConfig(
        minSdk = 26,
        targetSdk = 31,
        compileSdkVersion = 31,
        applicationId = "fr.sjcqs.wordle",
    )
    val jvm = JvmConfig(
        javaVersion = JavaVersion.VERSION_11,
        kotlinJvm = "11"
    )

    data class AndroidConfig(
        val minSdk: Int,
        val targetSdk: Int,
        val compileSdkVersion: Int,
        @Deprecated("TODO: remove and replace with an extension")
        val applicationId: String,
    )


    data class JvmConfig(
        val javaVersion: JavaVersion,
        val kotlinJvm: String
    )

}