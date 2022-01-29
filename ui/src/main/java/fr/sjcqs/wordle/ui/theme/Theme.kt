package fr.sjcqs.wordle.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

@Suppress("unused")
val ColorScheme.present: Color
    @Composable @ReadOnlyComposable
    get() = LocalTileColors.current.present

@Suppress("unused")
val ColorScheme.correct: Color
    @Composable @ReadOnlyComposable
    get() = LocalTileColors.current.correct

@Suppress("unused")
val ColorScheme.absent: Color
    @Composable @ReadOnlyComposable
    get() = LocalTileColors.current.absent

@Suppress("unused")
val ColorScheme.onPresent: Color
    @Composable @ReadOnlyComposable
    get() = LocalTileColors.current.onPresent

@Suppress("unused")
val ColorScheme.onCorrect: Color
    @Composable @ReadOnlyComposable
    get() = LocalTileColors.current.onCorrect

@Suppress("unused")
val ColorScheme.onAbsent: Color
    @Composable @ReadOnlyComposable
    get() = LocalTileColors.current.onAbsent

@Composable
fun WordleTheme(
    isInDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (isInDarkTheme) DarkColorScheme else LightColorScheme
    val tileColorScheme = if (isInDarkTheme) DarkTileColorScheme else LightTileColorScheme
    LocalTileColors.provides(tileColorScheme)
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}