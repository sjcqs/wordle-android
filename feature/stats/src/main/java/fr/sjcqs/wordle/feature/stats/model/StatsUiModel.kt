package fr.sjcqs.wordle.feature.stats.model

data class StatsUiModel(
    val played: String = "",
    val winRate: String = "",
    val currentStreak: String = "",
    val maxStreak: String = "",
    val distributions: Map<Int, Int> = emptyMap(),
)