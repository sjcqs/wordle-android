package fr.sjcqs.wordle.feature.settings.model

import androidx.annotation.StringRes
import fr.sjcqs.wordle.feature.settings.R

internal data class SettingsUiModel(
    val theme: ThemeUiModel = ThemeUiModel.System,
    val keyboardLayout: KeyboardLayoutUiModel = KeyboardLayoutUiModel.Azerty,
    val setTheme: (ThemeUiModel) -> Unit = {},
    val setLayout: (KeyboardLayoutUiModel) -> Unit = {},
)

internal enum class ThemeUiModel(@StringRes val labelRes: Int) {
    Dark(R.string.settings_theme_dark),
    Light(R.string.settings_theme_light),
    System(R.string.settings_theme_system)
}

internal enum class KeyboardLayoutUiModel(@StringRes val labelRes: Int) {
    Azerty(R.string.settings_keyboard_layout_azerty),
    Qwerty(R.string.settings_keyboard_layout_qwerty),
}