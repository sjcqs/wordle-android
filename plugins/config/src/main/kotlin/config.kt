import org.gradle.api.JavaVersion

val config = Config(
    android = AndroidConfig(
        minSdk = 26,
        targetSdk = 31,
        compileSdkVersion = 31,
        applicationId = "fr.sjcqs.wordle",
    ),
    jvm = JvmConfig(
        javaVersion = JavaVersion.VERSION_1_8,
        kotlinJvm = "1.8"
    ),
    version = VersionConfig(
        code = 1,
        name = "1.0"
    )
)

data class Config(
    val android: AndroidConfig,
    val jvm: JvmConfig,
    val version: VersionConfig
)

data class AndroidConfig(
    val minSdk: Int,
    val targetSdk: Int,
    val compileSdkVersion: Int,
    val applicationId: String,
)

data class VersionConfig(
    val name: String,
    val code: Int
)

data class JvmConfig(
    val javaVersion: JavaVersion,
    val kotlinJvm: String
)
