package fr.sjcqs.wordle.haptics

import android.media.AudioAttributes
import android.os.VibrationEffect
import android.os.VibrationEffect.createOneShot
import android.os.VibrationEffect.createWaveform
import android.os.Vibrator
import javax.inject.Inject

class VibratorHapticsController @Inject constructor(
    private val vibrator: Vibrator
) : HapticsController {
    override fun vibrate(effect: Vibration) {
        when (effect) {
            is Vibration.Predefined -> vibrate(effect)
            is Vibration.WaveForm -> vibrate(effect)
            is Vibration.OneShot -> vibrate(effect)
        }
    }

    private fun vibrate(predefined: Vibration.Predefined) {
        when {
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q -> {
                val effectId = when (predefined) {
                    Vibration.Predefined.Tick -> VibrationEffect.EFFECT_TICK
                    Vibration.Predefined.Click -> VibrationEffect.EFFECT_CLICK
                    Vibration.Predefined.DoubleClick -> VibrationEffect.EFFECT_DOUBLE_CLICK
                    Vibration.Predefined.HeavyClick -> VibrationEffect.EFFECT_HEAVY_CLICK
                }
                vibrator.vibrate(VibrationEffect.createPredefined(effectId))
            }
            else -> {
                val effect = when (predefined) {
                    Vibration.Predefined.Tick -> {
                        createOneShot(
                            TICK_DURATION,
                            -1
                        )
                    }
                    Vibration.Predefined.Click -> {
                        createOneShot(
                            CLICK_DURATION,
                            -1
                        )
                    }
                    Vibration.Predefined.DoubleClick -> {
                        createWaveform(
                            DOUBLE_CLICK_PATTERN,
                            DOUBLE_CLICK_AMPLITUDES,
                            -1
                        )
                    }
                    Vibration.Predefined.HeavyClick -> {
                        createOneShot(
                            HEAVY_CLICK_DURATION,
                            -1
                        )
                    }
                }
                vibrator.vibrate(effect)
            }
        }
    }

    private fun vibrate(effect: Vibration.WaveForm) {
        val amplitudes = effect.amplitudeTimings
            .map(AmplitudeTiming::amplitude)
            .toIntArray()
        val timings = effect.amplitudeTimings
            .map(AmplitudeTiming::timing)
            .toLongArray()
        val repeatMode = when (effect.repeatMode) {
            RepeatMode.Once -> -1
            RepeatMode.Infinite -> 0
        }
        val attributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build()
        vibrator.vibrate(createWaveform(timings, amplitudes, repeatMode), attributes)
    }

    private fun vibrate(predefined: Vibration.OneShot) {
        val (amplitude, timing) = predefined.amplitudeTiming
        vibrator.vibrate(createOneShot(timing, amplitude))
    }

    override fun cancel() {
        vibrator.cancel()
    }

    companion object {
        private const val TICK_DURATION = 20L
        private const val CLICK_DURATION = 75L
        private const val HEAVY_CLICK_DURATION = 200L

        private val DOUBLE_CLICK_PATTERN = longArrayOf(
            0,
            CLICK_DURATION,
            CLICK_DURATION,
            CLICK_DURATION
        )
        private const val DEFAULT_AMPLITUDE = -1

        private val DOUBLE_CLICK_AMPLITUDES = intArrayOf(
            DEFAULT_AMPLITUDE,
            DEFAULT_AMPLITUDE,
            DEFAULT_AMPLITUDE,
            DEFAULT_AMPLITUDE
        )
    }
}
