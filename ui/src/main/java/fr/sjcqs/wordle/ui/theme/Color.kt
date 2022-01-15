package fr.sjcqs.wordle.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.material3.contentColorFor as materialContentColorFor

internal object Colors {
    val Black = Color(0xFF000000)
    val Black800 = Color(0xFF1C1C1C)
    val Breakerbay = Color(0xFF4F9892)
    val Cuttyshark = Color(0xFF546E7A)
    val Montecarlo = Color(0xFF80CBC4)
    val Offwhite = Color(0xFFFCFCFC)
    val Oslogray = Color(0xFF808E95)
    val Persiangray = Color(0xFF007167)
    val Red200 = Color(0xFFCF6679)
    val Red600 = Color(0xFFB00020)
    val Sherpablue = Color(0xFF00534C)
    val White = Color(0xFFFFFFFF)
}

internal val LocalTileColors = staticCompositionLocalOf { TileColorScheme() }

@Stable
internal class TileColorScheme(
    present: Color = Color(201, 180, 88),
    onPresent: Color = Color.White,
    correct: Color = Color(106, 170, 100),
    onCorrect: Color = Color.White,
    absent: Color = Color(120, 124, 126),
    onAbsent: Color = Color.White,
) {
    var present by mutableStateOf(present, structuralEqualityPolicy())
        internal set
    var onPresent by mutableStateOf(onPresent, structuralEqualityPolicy())
        internal set
    var correct by mutableStateOf(correct, structuralEqualityPolicy())
        internal set
    var onCorrect by mutableStateOf(onCorrect, structuralEqualityPolicy())
        internal set
    var absent by mutableStateOf(absent, structuralEqualityPolicy())
        internal set
    var onAbsent by mutableStateOf(onAbsent, structuralEqualityPolicy())
        internal set
}

@Composable
@ReadOnlyComposable
fun contentColorFor(backgroundColor: Color): Color {
    return MaterialTheme.colorScheme.contentColorFor(backgroundColor = backgroundColor)
        .takeOrElse { LocalContentColor.current }
}

@Composable
@ReadOnlyComposable
fun ColorScheme.contentColorFor(backgroundColor: Color) = when (backgroundColor) {
    present -> onPresent
    correct -> onCorrect
    absent -> onAbsent
    else -> materialContentColorFor(backgroundColor)
}

internal val DarkTileColorScheme = TileColorScheme(
    present = Color(181, 159, 59),
    correct = Color(83, 141, 78),
    absent = Color(58, 58, 60),
)

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
    outline = Color(135, 138, 140)
)

internal val LightTileColorScheme = TileColorScheme()

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
    outline = Color(58, 58, 60)
)
