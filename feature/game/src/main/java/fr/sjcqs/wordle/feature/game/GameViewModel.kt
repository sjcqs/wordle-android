package fr.sjcqs.wordle.feature.game

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.sjcqs.wordle.data.game.GameRepository
import fr.sjcqs.wordle.data.game.entity.Game
import fr.sjcqs.wordle.extensions.emitIn
import fr.sjcqs.wordle.logger.Logger
import fr.sjcqs.wordle.ui.components.TileUiState
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
internal class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val logger: Logger
) : ViewModel() {

    private val _uiState = MutableStateFlow<GameUiState>(
        GameUiState.Loading(gameRepository.maxGuesses)
    )
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<GameUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val events = MutableSharedFlow<Event>()

    init {
        gameRepository.dailyGame
            .onEach(::onGameUpdated)
            .map(::map)
            .onEach(_uiState::emit)
            .launchIn(viewModelScope)

        events.distinctUntilChanged()
            .onEach(::handleEvent)
            .launchIn(viewModelScope)

        viewModelScope.launch {
            logger.d("Game: ${gameRepository.getStats()}")
        }
    }

    private fun onGameUpdated(game: Game) {
        if (game.isFinished) {
            _uiEvent.emitIn(viewModelScope, GameUiEvent.CloseKeyboard)
        }
    }

    private fun map(game: Game) = game.toUiState(
        onRetry = { events.emitIn(viewModelScope, Event.Retry) },
        onTyping = { events.emitIn(viewModelScope, Event.Typing) },
        onSubmit = { word -> events.emitIn(viewModelScope, Event.Submit(word)) },
    )

    private suspend fun handleEvent(event: Event) {
        when (event) {
            is Event.Retry -> retry()
            is Event.Submit -> submit(event.word)
            is Event.Typing -> _uiEvent.emitIn(viewModelScope, GameUiEvent.Dismiss)
        }
    }

    private suspend fun retry() {
        gameRepository.refresh()
    }

    private suspend fun submit(word: String) {
        val wasSubmitted = gameRepository.submit(word)
        if (!wasSubmitted) {
            _uiEvent.emitIn(viewModelScope, GameUiEvent.InvalidWord)
        } else {
            _uiEvent.emitIn(viewModelScope, GameUiEvent.ClearInput)
        }
    }

    private sealed interface Event {
        data class Submit(val word: String) : Event
        object Retry : Event
        object Typing : Event
    }
}

internal sealed interface GameUiState {
    @Immutable
    data class Loading(val maxGuesses: Int) : GameUiState

    sealed interface Playing : GameUiState {
        val guesses: List<GuessUiModel>
        val length: Int
    }

    @Immutable
    data class Guessing(
        override val guesses: List<GuessUiModel>,
        override val length: Int,
        val tilesLetters: Map<Int, Map<Char, TileUiState>>,
        val onTyping: () -> Unit,
        val onSubmit: (word: String) -> Unit,
        val isCurrentWordInvalid: Boolean = false,
    ) : Playing

    @Immutable
    data class Finished(
        val word: String,
        override val guesses: List<GuessUiModel>,
        val isWon: Boolean,
        override val length: Int,
        val onRetry: () -> Unit,
    ) : Playing
}

internal sealed interface GameUiEvent {
    object ClearInput : GameUiEvent
    object CloseKeyboard : GameUiEvent

    sealed interface Notify : GameUiEvent
    object InvalidWord : Notify
    object Dismiss : Notify
}

@Immutable
internal data class GuessUiModel(
    val word: String = "",
    val tileState: Map<Int, TileUiState> = emptyMap(),
    val isEditable: Boolean = false,
)
