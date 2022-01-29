package fr.sjcqs.wordle.data.settings.local.impl

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import fr.sjcqs.wordle.data.settings.entity.KeyboardLayout
import fr.sjcqs.wordle.data.settings.entity.Settings
import fr.sjcqs.wordle.data.settings.entity.Theme

internal object Keys {
    val keyboardLayout = stringPreferencesKey("keyboardLayout")
    val theme = stringPreferencesKey("darkMode")
}

internal val initialSettings = Settings(KeyboardLayout.Azerty, Theme.System)

fun Preferences.toSettings(): Settings {
    val keyboardLayout = this[Keys.keyboardLayout]
        ?.let(KeyboardLayout::valueOf)
        ?: initialSettings.keyboardLayout
    val theme = this[Keys.theme]
        ?.let(Theme::valueOf)
        ?: initialSettings.theme
    return Settings(keyboardLayout = keyboardLayout, theme = theme)
}