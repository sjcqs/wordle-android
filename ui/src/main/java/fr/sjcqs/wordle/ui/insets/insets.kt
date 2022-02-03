package fr.sjcqs.wordle.ui.insets

import androidx.compose.runtime.Composable
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.WindowInsets
import com.google.accompanist.insets.derivedWindowInsetsTypeOf

@Composable
fun statusBarAndDisplayCutoutInsets(): WindowInsets.Type {
    val windowInsets = LocalWindowInsets.current
    return derivedWindowInsetsTypeOf(
        windowInsets.statusBars,
        windowInsets.displayCutout
    )
}