package fr.sjcqs.wordle.feature.guessing

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.sjcqs.wordle.data.game.GameRepository
import fr.sjcqs.wordle.data.game.entity.Guess
import fr.sjcqs.wordle.ui.components.TileUiState
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
internal class GuessingViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<GuessingUiState>(GuessingUiState.Loading)
    val uiState: StateFlow<GuessingUiState> = _uiState

    init {
        gameRepository.dailyGame
            .onEach { game ->
                val guesses = game.guesses.map(Guess::toUiModel)
                _uiState.value = GuessingUiState.Guessing(guesses)
            }.launchIn(viewModelScope)
    }

    fun onSubmit(word: String) {
        viewModelScope.launch {
            gameRepository.submit(word)
        }
    }
}

@Immutable
internal sealed interface GuessingUiState {
    object Loading : GuessingUiState
    data class Guessing(val guesses: List<GuessUiModel>) : GuessingUiState
}

@Immutable
internal data class GuessUiModel(
    val word: String = "",
    val tileState: Map<Int, TileUiState> = emptyMap(),
    val isEditable: Boolean = false,
)
