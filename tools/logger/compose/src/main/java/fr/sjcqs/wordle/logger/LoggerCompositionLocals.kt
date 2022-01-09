package fr.sjcqs.wordle.logger

import androidx.compose.runtime.staticCompositionLocalOf

val LocalLogger = staticCompositionLocalOf<Logger> { NoOpLogger() }