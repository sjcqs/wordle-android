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
    onReload: () -> Unit,
    onSubmit: (String) -> Unit,
): GameUiState {
    val length = word.length
    val guessUiModels = buildList {
        addAll(guesses.map(Guess::toUiModel))
        if (!isFinished) {
            add(GuessUiModel(isEditable = true))
            repeat(guessesCount - size) {
                add(GuessUiModel())
            }
        }
    }
    return if (isFinished) {
        GameUiState.Finished(
            word = word,
            guesses = guessUiModels,
            length = length,
            isWon = isWon,
            onReload = onReload
        )
    } else {
        GameUiState.Guessing(
            guesses = guessUiModels,
            length = length,
            tilesLetters = tileLetters.mapValues { (_, letters) ->
                letters.mapValues { (_, tileState) -> tileState.toUiModel(isHint = true) }
            },
            onSubmit = onSubmit
        )
    }
}


internal fun Guess.toUiModel(): GuessUiModel = GuessUiModel(
    word = word,
    tileState = tiles.mapIndexed { index, state ->
        index to state.toUiModel()
    }.toMap()
)