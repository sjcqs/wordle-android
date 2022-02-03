package fr.sjcqs.wordle.feature.game.model

import androidx.compose.runtime.Immutable
import fr.sjcqs.wordle.feature.game.component.KeyboardLayoutUiModel
import fr.sjcqs.wordle.ui.components.TileUiState
import java.time.Duration
import kotlinx.coroutines.flow.StateFlow

internal sealed interface GameUiState {
    @Immutable
    data class Loading(val maxGuesses: Int) : GameUiState

    @Immutable
    data class Guessing(
        val guesses: List<GuessUiModel>,
        val word: String,
        val keyboardLayout: KeyboardLayoutUiModel = KeyboardLayoutUiModel.Azerty,
        val keyStates: Map<String, TileUiState> = emptyMap(),
        val isFinished: Boolean = false,
        val isWon: Boolean = false,
        val canRetry: Boolean = true,
        val sharedText: String = "",
        val expiredInFlow: StateFlow<Duration>? = null,
        val onTyping: () -> Unit = {},
        val onSubmit: (word: String) -> Unit = {},
        val onRetry: () -> Unit = {},
        val share: (text: String) -> Unit = {}
    ) : GameUiState
}