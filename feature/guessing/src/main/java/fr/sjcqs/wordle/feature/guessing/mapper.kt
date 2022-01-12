package fr.sjcqs.wordle.feature.guessing

import fr.sjcqs.wordle.data.game.entity.Game
import fr.sjcqs.wordle.data.game.entity.Guess
import fr.sjcqs.wordle.data.game.entity.TileState
import fr.sjcqs.wordle.ui.components.TileUiState

internal fun TileState.toUiModel(): TileUiState = when (this) {
    TileState.Correct -> TileUiState.Correct
    TileState.Absent -> TileUiState.Absent
    TileState.Present -> TileUiState.Present
}

internal fun Game.toUiModel(): GuessingUiState = GuessingUiState.Guessing(
    guesses = buildList {
        addAll(guesses.map(Guess::toUiModel))
        if (!isFinished) {
            add(GuessUiModel(isEditable = true))
        }
        repeat(guessesCount - size) {
            add(GuessUiModel())
        }
    },
    length = word.length,
    isFinished = isFinished
)

internal fun Guess.toUiModel(): GuessUiModel = GuessUiModel(
    word = word,
    tileState = tiles.mapIndexed { index, state ->
        index to state.toUiModel()
    }.toMap()
)