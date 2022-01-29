package fr.sjcqs.wordle.feature.settings.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.Scaffold
import fr.sjcqs.wordle.feature.settings.R
import fr.sjcqs.wordle.feature.settings.SettingsViewModel
import fr.sjcqs.wordle.feature.settings.model.KeyboardLayoutUiModel
import fr.sjcqs.wordle.feature.settings.model.SettingsUiModel
import fr.sjcqs.wordle.feature.settings.model.SettingsUiModelParameterProvider
import fr.sjcqs.wordle.feature.settings.model.ThemeUiModel
import fr.sjcqs.wordle.ui.components.CenterAlignedTopAppBar
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
        theme = uiModel.theme,
        keyboardLayout = uiModel.keyboardLayout,
        setTheme = uiModel.setTheme,
        setLayout = uiModel.setLayout
    )
}

@Composable
private fun Settings(
    modifier: Modifier = Modifier,
    theme: ThemeUiModel,
    keyboardLayout: KeyboardLayoutUiModel,
    setTheme: (ThemeUiModel) -> Unit,
    setLayout: (KeyboardLayoutUiModel) -> Unit,
) {
    Column(modifier) {
        Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(id = R.string.settings_theme_header),
                style = MaterialTheme.typography.labelLarge
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(id = R.string.settings_keyboard_layout_header),
                style = MaterialTheme.typography.labelLarge
            )
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
