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
    get() = LocalLetterColors.current.present

@Suppress("unused")
val ColorScheme.correct: Color
    @Composable @ReadOnlyComposable
    get() = LocalLetterColors.current.correct

@Suppress("unused")
val ColorScheme.absent: Color
    @Composable @ReadOnlyComposable
    get() = LocalLetterColors.current.absent

@Suppress("unused")
val ColorScheme.onPresent: Color
    @Composable @ReadOnlyComposable
    get() = LocalLetterColors.current.onPresent

@Suppress("unused")
val ColorScheme.onCorrect: Color
    @Composable @ReadOnlyComposable
    get() = LocalLetterColors.current.onCorrect

@Suppress("unused")
val ColorScheme.onAbsent: Color
    @Composable @ReadOnlyComposable
    get() = LocalLetterColors.current.onAbsent

@Composable
fun WordleTheme(
    isInDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (isInDarkTheme) DarkColorScheme else LightColorScheme
    val letterColorScheme = if (isInDarkTheme) DarkLetterColorScheme else LightLetterColorScheme
    LocalLetterColors.provides(letterColorScheme)
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}