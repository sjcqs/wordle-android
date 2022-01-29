package fr.sjcqs.wordle.data.settings

import fr.sjcqs.wordle.data.settings.entity.KeyboardLayout
import fr.sjcqs.wordle.data.settings.entity.Settings
import fr.sjcqs.wordle.data.settings.entity.Theme
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settingsFlow: Flow<Settings>
    val settings: Settings

    suspend fun setKeyboardLayout(keyboardLayout: KeyboardLayout)
    suspend fun setTheme(theme: Theme)
}