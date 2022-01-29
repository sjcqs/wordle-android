package fr.sjcqs.wordle.data.settings

import fr.sjcqs.wordle.data.settings.entity.Settings
import fr.sjcqs.wordle.data.settings.entity.Theme
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settingsFlow: Flow<Settings>

    fun setKeyboardLayout(keyboardLayout: String)
    fun setTheme(theme: Theme)
}