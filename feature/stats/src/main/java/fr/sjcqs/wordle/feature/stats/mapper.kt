package fr.sjcqs.wordle.feature.stats

import fr.sjcqs.wordle.data.game.entity.Stats
import fr.sjcqs.wordle.feature.stats.model.StatsUiModel
import java.text.NumberFormat

internal fun Stats.toUiModel(): StatsUiModel {
    val numberFormat = NumberFormat.getNumberInstance()
    return StatsUiModel(
        played = numberFormat.format(played),
        winRate = NumberFormat.getPercentInstance().format(winRate),
        currentStreak = numberFormat.format(currentStreak),
        maxStreak = numberFormat.format(maxStreak),
        distributions = distributions,
    )
}

