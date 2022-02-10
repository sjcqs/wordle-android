package fr.sjcqs.wordle.feature.settings.model

import fr.sjcqs.wordle.data.settings.entity.GameMode
import fr.sjcqs.wordle.data.settings.entity.KeyboardLayout
import fr.sjcqs.wordle.data.settings.entity.Settings
import fr.sjcqs.wordle.data.settings.entity.Theme

internal fun Settings.toUiModel(
    setTheme: (ThemeUiModel) -> Unit,
    setLayout: (KeyboardLayoutUiModel) -> Unit,
    setGameMode: (GameModeUiModel) -> Unit,
) = SettingsUiModel(
    theme = theme.fromEntity(),
    keyboardLayout = keyboardLayout.fromEntity(),
    mode = gameMode.fromEntity(),
    setTheme = setTheme,
    setLayout = setLayout,
    setGameMode = setGameMode,
)

internal fun ThemeUiModel.toEntity() = when (this) {
    ThemeUiModel.Dark -> Theme.Dark
    ThemeUiModel.Light -> Theme.Light
    ThemeUiModel.System -> Theme.System
}

internal fun Theme.fromEntity() = when (this) {
    Theme.Dark -> ThemeUiModel.Dark
    Theme.Light -> ThemeUiModel.Light
    Theme.System -> ThemeUiModel.System
}

internal fun KeyboardLayoutUiModel.toEntity() = when (this) {
    KeyboardLayoutUiModel.Azerty -> KeyboardLayout.Azerty
    KeyboardLayoutUiModel.Qwerty -> KeyboardLayout.Qwerty
}

internal fun KeyboardLayout.fromEntity() = when (this) {
    KeyboardLayout.Azerty -> KeyboardLayoutUiModel.Azerty
    KeyboardLayout.Qwerty -> KeyboardLayoutUiModel.Qwerty
}

internal fun GameMode.fromEntity() = when (this) {
    GameMode.Daily -> GameModeUiModel.Daily
    GameMode.Infinite -> GameModeUiModel.Infinite
}

internal fun GameModeUiModel.toEntity() = when (this) {
    GameModeUiModel.Daily -> GameMode.Daily
    GameModeUiModel.Infinite -> GameMode.Infinite
}
