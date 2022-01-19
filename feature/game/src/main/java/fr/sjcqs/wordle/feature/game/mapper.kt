package fr.sjcqs.wordle.feature.game

import fr.sjcqs.wordle.data.game.entity.Game
import fr.sjcqs.wordle.data.game.entity.Guess
import fr.sjcqs.wordle.data.game.entity.TileState
import fr.sjcqs.wordle.ui.components.TileUiState

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