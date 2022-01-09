package fr.sjcqs.wordle.haptics

import androidx.compose.runtime.staticCompositionLocalOf

val LocalHapticController = staticCompositionLocalOf<HapticsController> { NoOpHapticsController() }

private class NoOpHapticsController : HapticsController {
    override fun vibrate(effect: Vibration) {
        /* no-op */
    }

    override fun cancel() {
        /* no-op */
    }

}