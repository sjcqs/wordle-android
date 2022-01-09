package fr.sjcqs.wordle.haptics

enum class RepeatMode {
    Once, Infinite
}

data class AmplitudeTiming(val amplitude: Int, val timing: Long)

sealed class Vibration {
    sealed class Predefined : Vibration() {
        object Tick : Predefined()
        object Click : Predefined()
        object HeavyClick : Predefined()
        object DoubleClick : Predefined()
    }

    class WaveForm(
        val repeatMode: RepeatMode,
        val amplitudeTimings: List<AmplitudeTiming>
    ) : Vibration() {
        constructor(
            repeatMode: RepeatMode,
            vararg amplitudeTimings: Pair<Int, Long>
        ) : this(
            repeatMode,
            amplitudeTimings.map { AmplitudeTiming(it.first, it.second) }
        )
    }

    class OneShot(val amplitudeTiming: AmplitudeTiming) : Vibration()

    companion object {
        val DEFAULT_AMPLITUDE = -1
    }
}
