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
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive

@HiltViewModel
internal class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val logger: Logger,
) : ViewModel() {

    private val _uiState = MutableStateFlow<GameUiState>(
        GameUiState.Loading(gameRepository.maxGuesses)
    )
    val uiState = _uiState.asStateFlow()

    private val isCountdownVisibleFlow = MutableStateFlow(false)

    val statsFlow: StateFlow<StatsUiModel> = gameRepository.statsFlow
        .map { stats -> stats.toUiModel() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, StatsUiModel())

    private val _uiEvent = MutableSharedFlow<GameUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val events = MutableSharedFlow<Event>()

    init {
        gameRepository.dailyGameFlow
            .combineTransform(isCountdownVisibleFlow) { game, isCountdownVisible ->
                val uiModel = map(game)
                emit(uiModel)
                if (isCountdownVisible) {
                    while (currentCoroutineContext().isActive) {
                        emit(uiModel.updateExpiredIn(game.expiredAt))
                        delay(1000)
                    }
                }
            }.onEach(_uiState::emit)
            .launchIn(viewModelScope)

        events.onEach(::handleEvent)
            .launchIn(viewModelScope)
    }

    private fun map(game: Game) = game.toUiModel(
        onRetry = { events.emitIn(viewModelScope, Event.Retry) },
        onTyping = { events.emitIn(viewModelScope, Event.Typing) },
        onSubmit = { word -> events.emitIn(viewModelScope, Event.Submit(word)) },
        onStatsOpened = { events.emitIn(viewModelScope, Event.OnCountdownVisible) },
        onStatsDismissed = { events.emitIn(viewModelScope, Event.OnCountdownVisible) },
        onShare = { events.emitIn(viewModelScope, Event.OnShare(it)) }
    )

    private suspend fun handleEvent(event: Event) {
        when (event) {
            is Event.Retry -> retry()
            is Event.Submit -> submit(event.word)
            is Event.OnShare -> _uiEvent.emitIn(viewModelScope, GameUiEvent.Share(event.text))
            is Event.Typing -> _uiEvent.emitIn(viewModelScope, GameUiEvent.Dismiss)
            Event.OnCountdownHidden -> isCountdownVisibleFlow.emit(false)
            Event.OnCountdownVisible -> isCountdownVisibleFlow.emit(true)
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
        data class OnShare(val text: String) : Event
        object Retry : Event
        object Typing : Event
        object OnCountdownVisible : Event
        object OnCountdownHidden : Event
    }
}

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
        val stats: StatsUiModel = StatsUiModel(),
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

internal sealed interface GameUiEvent {
    object ClearInput : GameUiEvent

    data class Share(val text: String) : GameUiEvent

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

data class StatsUiModel(
    val played: String = "",
    val winRate: String = "",
    val currentStreak: String = "",
    val maxStreak: String = "",
    val distributions: Map<Int, Int> = emptyMap(),
)
