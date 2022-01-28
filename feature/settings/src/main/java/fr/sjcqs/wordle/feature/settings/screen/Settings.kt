package fr.sjcqs.wordle.feature.settings.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.Scaffold
import fr.sjcqs.wordle.feature.settings.R
import fr.sjcqs.wordle.feature.settings.SettingsViewModel
import fr.sjcqs.wordle.ui.components.CenterAlignedTopAppBar
import fr.sjcqs.wordle.ui.components.IconButton
import fr.sjcqs.wordle.ui.icons.Icons

@Composable
fun Settings(onClose: () -> Unit) {
    val viewModel: SettingsViewModel = hiltViewModel()

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
                // TODO: Add content
            }
        }
    )
}