package fr.sjcqs.wordle.feature.settings.model

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

internal class SettingsUiModelParameterProvider : PreviewParameterProvider<SettingsUiModel> {
    override val values: Sequence<SettingsUiModel> = sequenceOf(
        SettingsUiModel(ThemeUiModel.Dark, KeyboardLayoutUiModel.Azerty),
        SettingsUiModel(ThemeUiModel.Dark, KeyboardLayoutUiModel.Qwerty),
        SettingsUiModel(ThemeUiModel.Light, KeyboardLayoutUiModel.Azerty),
        SettingsUiModel(ThemeUiModel.Light, KeyboardLayoutUiModel.Qwerty),
    )

}