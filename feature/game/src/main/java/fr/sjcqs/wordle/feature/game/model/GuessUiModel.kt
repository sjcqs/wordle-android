package fr.sjcqs.wordle.feature.game.model

import androidx.compose.runtime.Immutable
import fr.sjcqs.wordle.ui.components.TileUiState

@Immutable
internal data class GuessUiModel(
    val word: String = "",
    val tileState: Map<Int, TileUiState> = emptyMap(),
    val isEditable: Boolean = false,
)