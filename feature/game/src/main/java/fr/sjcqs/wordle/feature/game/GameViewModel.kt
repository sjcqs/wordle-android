package fr.sjcqs.wordle.feature.game

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.sjcqs.wordle.data.game.GameRepository
import fr.sjcqs.wordle.data.game.entity.Game
import fr.sjcqs.wordle.ui.components.TileUiState
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
internal class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
) : ViewModel() {

    private val _uiStateFlow = MutableStateFlow<GameUiState>(GameUiState.Loading)
    val uiStateFlow = _uiStateFlow.asStateFlow()

    private val _uiEventFlow = MutableSharedFlow<GameUiEvent>()
    val uiEventFlow = _uiEventFlow.asSharedFlow()

    init {
        gameRepository.dailyGame.map(Game::toUiState)
            .onEach(_uiStateFlow::emit)
            .launchIn(viewModelScope)
    }

    fun onSubmit(word: String) {
        viewModelScope.launch {
            val wasSubmitted = gameRepository.submit(word)
            _uiEventFlow.emit(GameUiEvent.ClearInput)
            if (!wasSubmitted) {
                _uiEventFlow.emit(GameUiEvent.InvalidWord)
            }
        }
    }
}

internal sealed interface GameUiState {
    @Immutable
    object Loading : GameUiState

    sealed interface Playing : GameUiState {
        val guesses: List<GuessUiModel>
        val length: Int
    }

    @Immutable
    data class Guessing(
        override val guesses: List<GuessUiModel>,
        override val length: Int,
        val tilesLetters: Map<Int, Map<Char, TileUiState>>,
    ) : Playing

    @Immutable
    data class Finished(
        val word: String,
        override val guesses: List<GuessUiModel>,
        val isWon: Boolean,
        override val length: Int,
    ) : Playing
}

internal sealed interface GameUiEvent {
    object InvalidWord : GameUiEvent
    object ClearInput : GameUiEvent
}

@Immutable
internal data class GuessUiModel(
    val word: String = "",
    val tileState: Map<Int, TileUiState> = emptyMap(),
    val isEditable: Boolean = false,
)
