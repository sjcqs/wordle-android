package fr.sjcqs.wordle.data.settings.local.impl

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import fr.sjcqs.wordle.data.settings.entity.GameMode
import fr.sjcqs.wordle.data.settings.entity.KeyboardLayout
import fr.sjcqs.wordle.data.settings.entity.Settings
import fr.sjcqs.wordle.data.settings.entity.Theme

internal object Keys {
    val keyboardLayout = stringPreferencesKey("keyboardLayout")
    val theme = stringPreferencesKey("darkMode")
    val gameMode = stringPreferencesKey("gameMode")
}

internal val initialSettings = Settings()

fun Preferences.toSettings(): Settings {
    val keyboardLayout = this[Keys.keyboardLayout]?.let(KeyboardLayout::valueOf)
        ?: KeyboardLayout.Default
    val theme = this[Keys.theme]?.let(Theme::valueOf)
        ?: Theme.Default
    val gameMode = this[Keys.gameMode]?.let(GameMode::valueOf)
        ?: GameMode.Default

    return Settings(keyboardLayout = keyboardLayout, theme = theme, gameMode = gameMode)
}