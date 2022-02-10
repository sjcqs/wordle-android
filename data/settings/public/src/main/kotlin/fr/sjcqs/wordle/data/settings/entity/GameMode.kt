package fr.sjcqs.wordle.data.settings.entity

enum class GameMode {
    Infinite, Daily;

    companion object {
        val Default
            get() = Daily
    }
}