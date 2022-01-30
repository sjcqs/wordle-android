package fr.sjcqs.wordle.feature.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.sjcqs.wordle.data.game.GameRepository
import fr.sjcqs.wordle.data.game.entity.Game
import fr.sjcqs.wordle.data.settings.SettingsRepository
import fr.sjcqs.wordle.data.settings.entity.KeyboardLayout
import fr.sjcqs.wordle.extensions.emitIn
import fr.sjcqs.wordle.feature.game.model.GameUiEvent
import fr.sjcqs.wordle.feature.game.model.GameUiState
import javax.inject.Inject
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive

@HiltViewModel
internal class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<GameUiState>(
        GameUiState.Loading(gameRepository.maxGuesses)
    )
    val uiState = _uiState.asStateFlow()

    private val isCountdownVisibleFlow = MutableStateFlow(false)

    private val _uiEvent = MutableSharedFlow<GameUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val keyboardLayoutFlow = settingsRepository.settingsFlow.map { it.keyboardLayout }

    private val events = MutableSharedFlow<Event>()

    init {
        combineTransform(
            gameRepository.dailyGameFlow,
            isCountdownVisibleFlow,
            keyboardLayoutFlow
        ) { game, isCountdownVisible, keyboardLayout ->
            val uiModel = map(game, keyboardLayout)
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

    private fun map(game: Game, keyboardLayout: KeyboardLayout) = game.toUiModel(
        keyboardLayout = keyboardLayout,
        onRetry = { events.emitIn(viewModelScope, Event.Retry) },
        onTyping = { events.emitIn(viewModelScope, Event.Typing) },
        onSubmit = { word -> events.emitIn(viewModelScope, Event.Submit(word)) },
        onCountdownHidden = { events.emitIn(viewModelScope, Event.OnCountdownHidden) },
        onCountdownVisible = { events.emitIn(viewModelScope, Event.OnCountdownVisible) },
        onShare = { events.emitIn(viewModelScope, Event.OnShare(it)) }
    )

    private suspend fun handleEvent(event: Event) {
        when (event) {
            is Event.Retry -> retry()
            is Event.Submit -> submit(event.word)
            is Event.Typing -> _uiEvent.emitIn(viewModelScope, GameUiEvent.Dismiss)
            Event.OnCountdownHidden -> isCountdownVisibleFlow.emit(false)
            Event.OnCountdownVisible -> isCountdownVisibleFlow.emit(true)
            is Event.OnShare -> _uiEvent.emitIn(viewModelScope, GameUiEvent.Share(event.text))
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

