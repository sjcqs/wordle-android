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
    val Black800 = Color(0xFF1b1b1b)
    val Breakerbay = Color(0xFF4F9892)
    val Cuttyshark = Color(0xFF546E7A)
    val Montecarlo = Color(0xFF80CBC4)
    val Offwhite = Color(0xFFFBFCFA)
    val Oslogray = Color(0xFF878A8C)
    val Persiangray = Color(0xFF007167)
    val Red200 = Color(0xFFCF6679)
    val Red600 = Color(0xFFB00020)
    val Sherpablue = Color(0xFF00534C)
    val White = Color(0xFFFFFFFF)
    val Tussock = Color(0xFFB59F3B)
    val HippieGreen = Color(0xFF538D4E)
    val PixieGreen = Color(0xFFB8D8B6)
    val Tuna = Color(0xFF3A3A3C)
    val Sundance = Color(0xFFC9B458)
    val AquaForest = Color(0xFF6AAA64)
    val RollingStone = Color(0xFF84888A)
}

internal val LocalTileColors = staticCompositionLocalOf { TileColorScheme() }

@Stable
internal class TileColorScheme(
    present: Color = Colors.Sundance,
    onPresent: Color = Color.White,
    correct: Color = Colors.AquaForest,
    onCorrect: Color = Color.White,
    absent: Color = Colors.RollingStone,
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
    present = Colors.Tussock,
    correct = Colors.HippieGreen,
    absent = Colors.Tuna,
)

internal val DarkColorScheme = darkColorScheme(
    background = Colors.Black,
    error = Colors.Red200,
    onBackground = Colors.White,
    onError = Colors.Black,
    onPrimary = Colors.White,
    onSecondary = Color.White,
    onSurface = Colors.White,
    primary = Colors.HippieGreen,
    primaryContainer = Colors.PixieGreen,
    secondary = Colors.Tussock,
    secondaryContainer = Colors.Cuttyshark,
    surface = Colors.Black800,
    outline = Colors.Oslogray,
    tertiary = Colors.RollingStone,
    onTertiary = Colors.White,
)

internal val LightTileColorScheme = TileColorScheme(
    present = Colors.Sundance,
    onPresent = Color.White,
    correct = Colors.AquaForest,
    onCorrect = Color.White,
    absent = Colors.RollingStone,
    onAbsent = Color.White,
)

internal val LightColorScheme = lightColorScheme(
    background = Colors.Offwhite,
    error = Colors.Red600,
    onBackground = Color.Black,
    onError = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onSurface = Color.Black,
    primary = Colors.AquaForest,
    primaryContainer = Colors.Offwhite,
    secondary = Colors.Oslogray,
    secondaryContainer = Colors.Cuttyshark,
    surface = Colors.White,
    outline = Colors.Tuna,
    tertiary = Colors.RollingStone,
    onTertiary = Colors.White,
)
