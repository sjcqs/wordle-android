package fr.sjcqs.wordle.feature.game

import fr.sjcqs.wordle.data.game.entity.Game
import fr.sjcqs.wordle.data.game.entity.Guess
import fr.sjcqs.wordle.data.game.entity.Stats
import fr.sjcqs.wordle.data.game.entity.TileState
import fr.sjcqs.wordle.ui.components.TileUiState
import java.text.NumberFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal fun TileState.toUiModel(isHint: Boolean = false): TileUiState = when (this) {
    TileState.Correct -> if (isHint) TileUiState.HintCorrect else TileUiState.Correct
    TileState.Absent -> if (isHint) TileUiState.HintAbsent else TileUiState.Absent
    TileState.Present -> TileUiState.Present
}

internal fun Game.toUiState(
    onRetry: () -> Unit,
    onSubmit: (String) -> Unit,
    onTyping: () -> Unit,
): GameUiState {
    val guessUiModels = buildList {
        addAll(guesses.map(Guess::toUiModel))
        if (!isFinished) {
            add(GuessUiModel(isEditable = true))
            repeat(maxGuesses - size) {
                add(GuessUiModel())
            }
        }
    }
    val tilesLetters = tileLetters.mapValues { (_, letters) ->
        letters.mapValues { (_, tileState) -> tileState.toUiModel(isHint = true) }
    }

    return GameUiState.Guessing(
        guesses = guessUiModels,
        tilesLetters = tilesLetters,
        onTyping = onTyping,
        onSubmit = onSubmit,
        word = word,
        isFinished = isFinished,
        isWon = isWon,
        onRetry = onRetry,
        canRetry = true
    )
}


internal fun Guess.toUiModel(): GuessUiModel = GuessUiModel(
    word = word,
    tileState = tiles.mapIndexed { index, state ->
        index to state.toUiModel()
    }.toMap()
)

internal fun Stats.toUiModel(
    dailyFinishedGame: Game?,
    onStatsDismissed: () -> Unit,
    onStatsOpened: () -> Unit
): StatsUiModel {
    val numberFormat = NumberFormat.getNumberInstance()
    return StatsUiModel(
        played = numberFormat.format(played),
        winRate = NumberFormat.getPercentInstance().format(winRate),
        currentStreak = numberFormat.format(currentStreak),
        maxStreak = numberFormat.format(maxStreak),
        distributions = distributions,
        dailyWord = dailyFinishedGame?.word,
        expiredIn = dailyFinishedGame?.expiredAt?.let { expiredAt ->
            Duration.between(LocalDateTime.now(), expiredAt.atStartOfDay())
        },
        sharedText = sharedText(dailyFinishedGame),
        onStatsDismissed = onStatsDismissed,
        onStatsOpened = onStatsOpened
    )
}

fun sharedText(dailyFinishedGame: Game?): String? = dailyFinishedGame?.run {
    buildString {
        val date = expiredAt.minusDays(1)
            .format(DateTimeFormatter.ofPattern("DD MMM"))
        val guesses = guesses
        val performance = "${if (isWon) guesses.size else "\uD83D\uDC80"}/${maxGuesses}"
        appendLine("Le Mot (Wordle FR)")
        appendLine("le $date - $performance")
        appendLine()
        guesses.forEach { guess ->
            guess.tiles.forEach { tile ->
                when (tile) {
                    TileState.Correct -> append("\uD83D\uDFE9")
                    TileState.Absent -> append("⬛")
                    TileState.Present -> append("\uD83D\uDFE8")
                }
            }
            appendLine()
        }
        appendLine()
        appendLine("wordlefr.page.link/app (Android)")
    }
}
