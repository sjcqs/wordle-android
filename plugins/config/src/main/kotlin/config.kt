import org.gradle.api.JavaVersion

val config = Config(
    android = AndroidConfig(
        minSdk = 26,
        targetSdk = 31,
        compileSdkVersion = 31,
        applicationId = "fr.sjcqs.wordle",
    ),
    jvm = JvmConfig(
        javaVersion = JavaVersion.VERSION_11,
        kotlinJvm = "11"
    ),
)

data class Config(
    val android: AndroidConfig,
    val jvm: JvmConfig,
)

data class AndroidConfig(
    val minSdk: Int,
    val targetSdk: Int,
    val compileSdkVersion: Int,
    val applicationId: String,
)


data class JvmConfig(
    val javaVersion: JavaVersion,
    val kotlinJvm: String
)
