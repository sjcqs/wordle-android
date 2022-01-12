package fr.sjcqs.wordle.feature.guessing

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.sjcqs.wordle.data.game.GameRepository
import fr.sjcqs.wordle.ui.components.TileUiState
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
internal class GuessingViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _uiStateFlow = MutableStateFlow<GuessingUiState>(GuessingUiState.Loading)
    val uiStateFlow = _uiStateFlow.asStateFlow()

    private val _uiEventFlow = MutableSharedFlow<GuessingUiEvent>()
    val uiEventFlow = _uiEventFlow.asSharedFlow()

    init {
        gameRepository.dailyGame
            .onEach { game ->
                _uiStateFlow.value = game.toUiModel()
            }.launchIn(viewModelScope)
    }

    fun onSubmit(word: String) {
        viewModelScope.launch {
            val wasSubmitted = gameRepository.submit(word)
            if (!wasSubmitted) {
                _uiEventFlow.emit(GuessingUiEvent.ClearInput)
            }
        }
    }
}

internal sealed interface GuessingUiState {
    @Immutable
    object Loading : GuessingUiState

    @Immutable
    data class Guessing(
        val guesses: List<GuessUiModel>,
        val isFinished: Boolean,
        val length: Int,
    ) : GuessingUiState
}

internal sealed interface GuessingUiEvent {
    object ClearInput : GuessingUiEvent
}

@Immutable
internal data class GuessUiModel(
    val word: String = "",
    val tileState: Map<Int, TileUiState> = emptyMap(),
    val isEditable: Boolean = false,
)
