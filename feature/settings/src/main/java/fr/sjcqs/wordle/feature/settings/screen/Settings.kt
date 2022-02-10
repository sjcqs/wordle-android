package fr.sjcqs.wordle.feature.settings.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.Scaffold
import fr.sjcqs.wordle.feature.settings.R
import fr.sjcqs.wordle.feature.settings.SettingsViewModel
import fr.sjcqs.wordle.feature.settings.components.Chip
import fr.sjcqs.wordle.feature.settings.model.GameModeUiModel
import fr.sjcqs.wordle.feature.settings.model.KeyboardLayoutUiModel
import fr.sjcqs.wordle.feature.settings.model.SettingsUiModel
import fr.sjcqs.wordle.feature.settings.model.SettingsUiModelParameterProvider
import fr.sjcqs.wordle.feature.settings.model.ThemeUiModel
import fr.sjcqs.wordle.ui.components.CenterAlignedTopAppBar
import fr.sjcqs.wordle.ui.components.Divider
import fr.sjcqs.wordle.ui.components.IconButton
import fr.sjcqs.wordle.ui.icons.Icons
import fr.sjcqs.wordle.ui.theme.WordleTheme

@Composable
fun Settings(onClose: () -> Unit) {
    val viewModel: SettingsViewModel = hiltViewModel()

    val uiModel by viewModel.settingsFlow.collectAsState()
    Scaffold(
        topBar = {
            val closeOnClickLabel = stringResource(id = R.string.settings_label_close)
            CenterAlignedTopAppBar(
                contentPadding = rememberInsetsPaddingValues(
                    insets = LocalWindowInsets.current.statusBars
                ),
                shadowElevation = 4.dp,
                navigationIcon = {
                    IconButton(
                        onClick = onClose,
                        onClickLabel = closeOnClickLabel
                    ) {
                        Icons.Close()
                    }
                },
                title = { Text(text = stringResource(R.string.settings_title)) },
            )
        },
        content = { paddingValues ->
            Surface(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Settings(
                    modifier = Modifier.fillMaxSize(),
                    uiModel
                )
            }
        }
    )
}

@Composable
private fun Settings(
    modifier: Modifier = Modifier,
    uiModel: SettingsUiModel
) {
    Settings(
        modifier = modifier,
        currentTheme = uiModel.theme,
        currentLayout = uiModel.keyboardLayout,
        currentMode = uiModel.mode,
        setTheme = uiModel.setTheme,
        setLayout = uiModel.setLayout,
        setMode = uiModel.setGameMode
    )
}

@Composable
private fun Settings(
    modifier: Modifier = Modifier,
    currentTheme: ThemeUiModel,
    currentLayout: KeyboardLayoutUiModel,
    currentMode: GameModeUiModel,
    setTheme: (ThemeUiModel) -> Unit,
    setLayout: (KeyboardLayoutUiModel) -> Unit,
    setMode: (GameModeUiModel) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .semantics { heading() },
            text = stringResource(id = R.string.settings_preferences_header),
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(24.dp))
        SelectablePreference(
            title = stringResource(id = R.string.settings_theme_header),
            currentValue = currentTheme,
            onValueSelected = setTheme,
            values = ThemeUiModel.values().toList(),
            getLabel = { theme -> stringResource(id = theme.labelRes) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        SelectablePreference(
            title = stringResource(id = R.string.settings_keyboard_layout_header),
            currentValue = currentLayout,
            onValueSelected = setLayout,
            values = KeyboardLayoutUiModel.values().toList(),
            getLabel = { layout -> stringResource(id = layout.labelRes) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        SelectablePreference(
            title = stringResource(id = R.string.settings_mode_header),
            currentValue = currentMode,
            onValueSelected = setMode,
            values = GameModeUiModel.values().toList(),
            getLabel = { mode -> stringResource(id = mode.labelRes) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.align(CenterHorizontally),
            text = stringResource(id = currentMode.descriptionRes),
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        Divider()
        Spacer(modifier = Modifier.navigationBarsHeight())
    }
}

@Composable
private fun <T : Any> SelectablePreference(
    title: String,
    currentValue: T,
    onValueSelected: (T) -> Unit,
    values: Collection<T>,
    getLabel: @Composable (value: T) -> String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            values.forEachIndexed { index, theme ->
                Chip(
                    text = getLabel(theme),
                    isSelected = currentValue == theme,
                    onClick = { onValueSelected(theme) }
                )
                if (index + 1 < values.size) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun DarkSettingsPreview(
    @PreviewParameter(SettingsUiModelParameterProvider::class)
    uiModel: SettingsUiModel
) {
    WordleTheme(true) {
        Settings(
            uiModel = uiModel,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun LightSettingsPreview(
    @PreviewParameter(SettingsUiModelParameterProvider::class)
    uiModel: SettingsUiModel
) {
    WordleTheme(false) {
        Settings(
            uiModel = uiModel,
            modifier = Modifier.fillMaxSize()
        )
    }
}
