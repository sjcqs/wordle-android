package fr.sjcqs.wordle.data.settings

import fr.sjcqs.wordle.annotations.DefaultDispatcher
import fr.sjcqs.wordle.data.settings.entity.KeyboardLayout
import fr.sjcqs.wordle.data.settings.entity.Settings
import fr.sjcqs.wordle.data.settings.entity.Theme
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SettingsRepositoryImpl @Inject constructor(
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
) : SettingsRepository {
    override val settingsFlow: Flow<Settings> = flow {
        emit(initialSettings)
    }

    override fun setKeyboardLayout(keyboardLayout: String) {
        TODO("Not yet implemented")
    }

    override fun setTheme(theme: Theme) {
        TODO("Not yet implemented")
    }

    companion object {
        private val initialSettings = Settings(KeyboardLayout.Azerty, Theme.System)
    }
}