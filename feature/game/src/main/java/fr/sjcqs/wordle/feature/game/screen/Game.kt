package fr.sjcqs.wordle.feature.game.screen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.insets.ui.Scaffold
import fr.sjcqs.wordle.feature.game.GameUiEvent
import fr.sjcqs.wordle.feature.game.GameUiState
import fr.sjcqs.wordle.feature.game.GameViewModel
import fr.sjcqs.wordle.feature.game.R
import fr.sjcqs.wordle.feature.game.component.Finished
import fr.sjcqs.wordle.feature.game.component.Guessing
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Game() {
    val invalidWord = stringResource(id = R.string.guessing_invalid_word)

    val viewModel: GameViewModel = hiltViewModel()
    val uiState by viewModel.uiStateFlow.collectAsState()
    val ime = LocalWindowInsets.current.ime
    val elevation: Dp by remember { derivedStateOf { if (ime.isVisible) 4.dp else 0.dp } }
    val scaffoldState = rememberScaffoldState()
    val snackbarState by derivedStateOf { scaffoldState.snackbarHostState }
    val coroutineScope = rememberCoroutineScope()

    fun snackbar(message: String) {
        coroutineScope.launch {
            snackbarState.showSnackbar(message)
        }
    }

    val (typingWord, setTypingWord) = remember(viewModel) {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = viewModel) {
        viewModel.uiEventFlow.collect { event ->
            when (event) {
                GameUiEvent.ClearInput -> setTypingWord("")
                GameUiEvent.InvalidWord -> snackbar(invalidWord)
                GameUiEvent.Dismiss -> snackbarState.currentSnackbarData?.dismiss()
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(
                hostState = it,
                modifier = Modifier.navigationBarsWithImePadding()
            )
        },
        topBar = {
            Surface(
                shadowElevation = elevation,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(vertical = 16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.game_title),
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.headlineLarge,
                    )
                }
            }
        },
        content = { paddingValues ->
            val scrollState = rememberScrollState()
            Surface(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(scrollState, enabled = ime.isVisible)
            ) {
                Game(
                    uiState = uiState,
                    typingWord = typingWord,
                    onValueChanged = setTypingWord,
                    scrollState = scrollState
                )
            }
        })
}

@Composable
private fun Game(
    uiState: GameUiState,
    typingWord: String,
    onValueChanged: (String) -> Unit,
    scrollState: ScrollState,
) {
    when (uiState) {
        is GameUiState.Guessing -> Guessing(
            uiState = uiState,
            value = typingWord,
            onValueChanged = onValueChanged,
            isFinished = false,
            scrollState = scrollState,
        )
        GameUiState.Loading -> CircularProgressIndicator()
        is GameUiState.Finished -> Finished(uiState = uiState)
    }
}

