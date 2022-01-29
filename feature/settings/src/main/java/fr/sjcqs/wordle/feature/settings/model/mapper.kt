package fr.sjcqs.wordle.feature.settings.model

import fr.sjcqs.wordle.data.settings.entity.KeyboardLayout
import fr.sjcqs.wordle.data.settings.entity.Settings
import fr.sjcqs.wordle.data.settings.entity.Theme

internal fun Settings.toUiModel(
    setTheme: (ThemeUiModel) -> Unit,
    setLayout: (KeyboardLayoutUiModel) -> Unit,
) = SettingsUiModel(
    theme = when (theme) {
        Theme.Dark -> ThemeUiModel.Dark
        Theme.Light -> ThemeUiModel.Light
        Theme.System -> ThemeUiModel.System
    },
    keyboardLayout = when (keyboardLayout) {
        KeyboardLayout.Qwerty -> KeyboardLayoutUiModel.Qwerty
        KeyboardLayout.Azerty -> KeyboardLayoutUiModel.Azerty
    },
    setTheme = setTheme,
    setLayout = setLayout
)

internal fun ThemeUiModel.toEntity() = when (this) {
    ThemeUiModel.Dark -> Theme.Dark
    ThemeUiModel.Light -> Theme.Light
    ThemeUiModel.System -> Theme.System
}

internal fun KeyboardLayoutUiModel.toEntity() = when (this) {
    KeyboardLayoutUiModel.Azerty -> KeyboardLayout.Azerty
    KeyboardLayoutUiModel.Qwerty -> KeyboardLayout.Qwerty
}