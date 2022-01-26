package fr.sjcqs.wordle.feature.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.sjcqs.wordle.data.game.GameRepository
import fr.sjcqs.wordle.extensions.emitIn
import fr.sjcqs.wordle.feature.stats.model.StatsUiEvent
import fr.sjcqs.wordle.feature.stats.model.StatsUiModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
internal class StatsViewModel @Inject constructor(gameRepository: GameRepository) : ViewModel() {

    val statsFlow: StateFlow<StatsUiModel> = gameRepository.statsFlow
        .map { stats -> stats.toUiModel() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, StatsUiModel())

    private val _uiEvent = MutableSharedFlow<StatsUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val events = MutableSharedFlow<Event>()

    init {
        events.onEach(::handleEvent).launchIn(viewModelScope)
    }

    private fun handleEvent(event: Event) {
        when (event) {
            is Event.OnShare -> _uiEvent.emitIn(viewModelScope, StatsUiEvent.Share(event.text))
        }
    }

    private sealed interface Event {
        data class OnShare(val text: String) : Event
    }
}

