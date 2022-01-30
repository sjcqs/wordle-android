package fr.sjcqs.wordle.feature.game.screen

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.Scaffold
import fr.sjcqs.wordle.feature.game.GameViewModel
import fr.sjcqs.wordle.feature.game.R
import fr.sjcqs.wordle.feature.game.component.Guessing
import fr.sjcqs.wordle.feature.game.model.GameUiEvent
import fr.sjcqs.wordle.feature.game.model.GameUiState
import fr.sjcqs.wordle.feature.game.model.GuessUiModel
import fr.sjcqs.wordle.haptics.LocalHapticController
import fr.sjcqs.wordle.haptics.doubleClick
import fr.sjcqs.wordle.ui.components.CenterAlignedTopAppBar
import fr.sjcqs.wordle.ui.components.IconButton
import fr.sjcqs.wordle.ui.icons.Icons
import kotlinx.coroutines.launch


@Composable
fun Game(showStats: () -> Unit, showSettings: () -> Unit) {
    val invalidWord = stringResource(id = R.string.guessing_invalid_word)

    val viewModel: GameViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    val scaffoldState = rememberScaffoldState()
    val snackbarState by derivedStateOf { scaffoldState.snackbarHostState }
    val coroutineScope = rememberCoroutineScope()

    fun snackbar(message: String) {
        coroutineScope.launch {
            snackbarState.currentSnackbarData?.dismiss()
            snackbarState.showSnackbar(message)
        }
    }

    val context = LocalContext.current
    val hapticsController = LocalHapticController.current
    val (typingWord, setTypingWord) = remember(viewModel) { mutableStateOf("") }
    LaunchedEffect(key1 = viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                GameUiEvent.ClearInput -> setTypingWord("")
                GameUiEvent.InvalidWord -> {
                    hapticsController.doubleClick()
                    snackbar(invalidWord)
                }
                GameUiEvent.Dismiss -> snackbarState.currentSnackbarData?.dismiss()
                is GameUiEvent.Share -> {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, event.text)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(
                hostState = it,
                modifier = Modifier.navigationBarsPadding()
            )
        },
        topBar = {
            CenterAlignedTopAppBar(
                contentPadding = rememberInsetsPaddingValues(
                    insets = LocalWindowInsets.current.statusBars
                ),
                shadowElevation = 4.dp,
                title = { Text(text = stringResource(id = R.string.game_title)) },
                actions = {
                    val statsClickLabel = stringResource(id = R.string.game_open_stats_label)
                    IconButton(onClick = showStats, onClickLabel = statsClickLabel) {
                        Icons.Stats()
                    }
                    val settingsClickLabel = stringResource(id = R.string.game_open_settings_label)
                    IconButton(onClick = showSettings, onClickLabel = settingsClickLabel) {
                        Icons.Settings()
                    }
                }
            )
        },
        content = { paddingValues ->
            Surface(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Game(
                    modifier = Modifier.fillMaxSize(),
                    state = uiState,
                    typingWord = typingWord,
                    onValueChanged = setTypingWord,
                )
            }
        }
    )
}

@Composable
private fun Game(
    state: GameUiState,
    modifier: Modifier = Modifier,
    typingWord: String,
    onValueChanged: (String) -> Unit,
) {
    when (state) {
        is GameUiState.Guessing -> Guessing(
            uiState = state,
            modifier = modifier,
            value = typingWord,
            onValueChanged = onValueChanged,
        )
        is GameUiState.Loading -> Guessing(
            modifier = modifier,
            uiState = GameUiState.Guessing(
                buildList { repeat(state.maxGuesses) { add(GuessUiModel("TESTE")) } },
                "TESTE"
            ),
            value = typingWord,
            onValueChanged = onValueChanged,
            isLoading = true
        )
    }
}
