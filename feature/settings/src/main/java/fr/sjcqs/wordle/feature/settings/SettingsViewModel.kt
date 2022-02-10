package fr.sjcqs.wordle.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.sjcqs.wordle.data.settings.SettingsRepository
import fr.sjcqs.wordle.extensions.emitIn
import fr.sjcqs.wordle.feature.settings.model.GameModeUiModel
import fr.sjcqs.wordle.feature.settings.model.KeyboardLayoutUiModel
import fr.sjcqs.wordle.feature.settings.model.SettingsUiModel
import fr.sjcqs.wordle.feature.settings.model.ThemeUiModel
import fr.sjcqs.wordle.feature.settings.model.toEntity
import fr.sjcqs.wordle.feature.settings.model.toUiModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {
    private val events = MutableSharedFlow<Event>()

    val settingsFlow = repository.settingsFlow
        .map { settings ->
            settings.toUiModel(
                setTheme = { events.emitIn(viewModelScope, Event.SetTheme(it)) },
                setLayout = { events.emitIn(viewModelScope, Event.SetKeyboardLayout(it)) },
                setGameMode = { events.emitIn(viewModelScope, Event.SetGameMode(it)) }
            )
        }.stateIn(viewModelScope, SharingStarted.Eagerly, SettingsUiModel())

    init {
        events.onEach(::handleEvent).launchIn(viewModelScope)
    }

    private suspend fun handleEvent(event: Event) {
        when (event) {
            is Event.SetKeyboardLayout -> repository.setKeyboardLayout(event.layout.toEntity())
            is Event.SetTheme -> repository.setTheme(event.theme.toEntity())
            is Event.SetGameMode -> repository.setMode(event.mode.toEntity())
        }
    }


    private sealed interface Event {
        data class SetGameMode(val mode: GameModeUiModel) : Event
        data class SetTheme(val theme: ThemeUiModel) : Event
        data class SetKeyboardLayout(val layout: KeyboardLayoutUiModel) : Event
    }
}

