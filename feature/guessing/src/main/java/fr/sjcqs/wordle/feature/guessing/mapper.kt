package fr.sjcqs.wordle.feature.guessing

import fr.sjcqs.wordle.data.game.entity.Guess
import fr.sjcqs.wordle.data.game.entity.TileState
import fr.sjcqs.wordle.ui.components.TileUiState

internal fun TileState.toUiState(): TileUiState = when (this) {
    TileState.Correct -> TileUiState.Correct
    TileState.Absent -> TileUiState.Absent
    TileState.Present -> TileUiState.Present
}

internal fun Guess.toUiModel(): GuessUiModel = when (this) {
    is Guess.Current -> GuessUiModel(isEditable = true)
    is Guess.Empty -> GuessUiModel()
    is Guess.Submitted -> GuessUiModel(
        word = word,
        tileState = tiles.mapIndexed { index, state ->
            index to state.toUiState()
        }.toMap()
    )
}