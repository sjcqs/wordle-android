package fr.sjcqs.wordle.feature.game.model

import androidx.compose.runtime.Immutable
import fr.sjcqs.wordle.ui.components.TileUiState
import java.time.Duration
import java.time.LocalDateTime

internal sealed interface GameUiState {
    @Immutable
    data class Loading(val maxGuesses: Int) : GameUiState

    @Immutable
    data class Guessing(
        val guesses: List<GuessUiModel>,
        val word: String,
        val keyStates: Map<String, TileUiState>,
        val isFinished: Boolean = false,
        val isWon: Boolean = false,
        val canRetry: Boolean = true,
        val sharedText: String = "",
        val expiredIn: Duration = Duration.ZERO,
        val onTyping: () -> Unit,
        val onSubmit: (word: String) -> Unit,
        val onRetry: () -> Unit,
        val onCountdownVisible: () -> Unit = {},
        val onCountdownHidden: () -> Unit = {},
        val share: (text: String) -> Unit = {}
    ) : GameUiState {
        fun updateExpiredIn(expiredAt: LocalDateTime) = copy(
            expiredIn = expiredAt.let {
                Duration.between(LocalDateTime.now(), expiredAt)
            }
        )
    }
}