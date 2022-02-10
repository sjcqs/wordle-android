package fr.sjcqs.wordle.feature.settings.model

import androidx.annotation.StringRes
import fr.sjcqs.wordle.data.settings.entity.GameMode
import fr.sjcqs.wordle.data.settings.entity.KeyboardLayout
import fr.sjcqs.wordle.data.settings.entity.Theme
import fr.sjcqs.wordle.feature.settings.R

internal data class SettingsUiModel(
    val theme: ThemeUiModel = ThemeUiModel.Default,
    val keyboardLayout: KeyboardLayoutUiModel = KeyboardLayoutUiModel.Default,
    val mode: GameModeUiModel = GameModeUiModel.Default,
    val setTheme: (ThemeUiModel) -> Unit = {},
    val setLayout: (KeyboardLayoutUiModel) -> Unit = {},
    val setGameMode: (GameModeUiModel) -> Unit = {}
)

internal enum class ThemeUiModel(@StringRes val labelRes: Int) {
    Dark(R.string.settings_theme_dark),
    Light(R.string.settings_theme_light),
    System(R.string.settings_theme_system);

    companion object {
        val Default
            get() = Theme.Default.fromEntity()
    }
}

internal enum class GameModeUiModel(
    @StringRes val labelRes: Int,
    @StringRes val descriptionRes: Int
) {
    Daily(R.string.settings_mode_daily, R.string.settings_mode_daily_description),
    Infinite(R.string.settings_mode_infinite, R.string.settings_mode_infinite_description);

    companion object {
        val Default
            get() = GameMode.Default.fromEntity()
    }
}

internal enum class KeyboardLayoutUiModel(@StringRes val labelRes: Int) {
    Azerty(R.string.settings_keyboard_layout_azerty),
    Qwerty(R.string.settings_keyboard_layout_qwerty);

    companion object {
        val Default
            get() = KeyboardLayout.Default.fromEntity()
    }
}