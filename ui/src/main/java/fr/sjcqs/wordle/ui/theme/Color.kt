package fr.sjcqs.wordle.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

internal object Colors {
    val Black = Color(0xFF000000)
    val Black800 = Color(0xFF1C1C1C)
    val Breakerbay = Color(0xFF4F9892)
    val BrightTurquoise = Color(0xFF11EDDB)
    val Brightturquoise = Color(0xFF11EDDB)
    val Charlotte = Color(0xFFB8FAF4)
    val Cuttyshark = Color(0xFF546E7A)
    val IceCold = Color(0xFFB4F3ED)
    val Icecold = Color(0xFFB4F3ED)
    val Montecarlo = Color(0xFF80CBC4)
    val Offwhite = Color(0xFFFCFCFC)
    val Oslogray = Color(0xFF808E95)
    val Persiangray = Color(0xFF007167)
    val Pomegranate = Color(0xFFED5911)
    val Purple200 = Color(0xFFBB86FC)
    val Purple500 = Color(0xFF6200EE)
    val Purple700 = Color(0xFF3700B3)
    val Red200 = Color(0xFFCF6679)
    val Red600 = Color(0xFFB00020)
    val RobinEggBlue = Color(0xFF05D6C4)
    val Robineggblue = Color(0xFF05D6C4)
    val Sherpablue = Color(0xFF00534C)
    val Teal200 = Color(0xFF03DAC5)
    val Teal700 = Color(0xFF018786)
    val Trinidad = Color(0xFFE53E0A)
    val White = Color(0xFFFFFFFF)
}

internal val DarkColorScheme = darkColorScheme(
    background = Colors.Black,
    error = Colors.Red200,
    onBackground = Colors.White,
    onError = Colors.Black,
    onPrimary = Colors.Black,
    onSecondary = Color.Black,
    onSurface = Colors.White,
    primary = Colors.Persiangray,
    primaryContainer = Colors.Breakerbay,
    secondary = Colors.Oslogray,
    secondaryContainer = Colors.Cuttyshark,
    surface = Colors.Black800,
)

internal val LightColorScheme = lightColorScheme(
    background = Colors.Offwhite,
    error = Colors.Red600,
    onBackground = Color.Black,
    onError = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onSurface = Color.Black,
    primary = Colors.Montecarlo,
    primaryContainer = Colors.Sherpablue,
    secondary = Colors.Oslogray,
    secondaryContainer = Colors.Cuttyshark,
    surface = Colors.White,
)
