package fr.sjcqs.wordle.data.game.entity

data class Stats(
    val played: Int,
    val winRate: Double,
    val currentStreak: Int,
    val maxStreak: Int,
    val distributions: Map<Int, Int>,
    val dailyFinishedGame: Game?,
)
