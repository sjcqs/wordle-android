package fr.sjcqs.wordle.feature.game.screen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MediumTopAppBar
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import fr.sjcqs.wordle.feature.game.SpaceBetweenGuesses
import fr.sjcqs.wordle.feature.game.component.Finished
import fr.sjcqs.wordle.feature.game.component.Guessing
import fr.sjcqs.wordle.ui.components.Word
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun Game() {
    val invalidWord = stringResource(id = R.string.guessing_invalid_word)

    val viewModel: GameViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val ime = LocalWindowInsets.current.ime
    val elevation: Dp by remember { derivedStateOf { if (ime.isVisible) 4.dp else 0.dp } }
    val scaffoldState = rememberScaffoldState()
    val snackbarState by derivedStateOf { scaffoldState.snackbarHostState }
    val coroutineScope = rememberCoroutineScope()

    val keyboard = LocalSoftwareKeyboardController.current

    val closeKeyboard = {
        keyboard?.hide()
    }

    fun snackbar(message: String) {
        coroutineScope.launch {
            snackbarState.showSnackbar(message)
        }
    }

    val (typingWord, setTypingWord) = remember(viewModel) {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                GameUiEvent.ClearInput -> setTypingWord("")
                GameUiEvent.InvalidWord -> snackbar(invalidWord)
                GameUiEvent.Dismiss -> snackbarState.currentSnackbarData?.dismiss()
                GameUiEvent.CloseKeyboard -> closeKeyboard()
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
            MediumTopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.Center),
                            text = stringResource(id = R.string.game_title)
                        )
                    }
                }
            )
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
                    state = uiState,
                    typingWord = typingWord,
                    onValueChanged = setTypingWord,
                    scrollState = scrollState
                )
            }
        })
}

@Composable
private fun Game(
    state: GameUiState,
    typingWord: String,
    onValueChanged: (String) -> Unit,
    scrollState: ScrollState,
) {
    when (state) {
        is GameUiState.Guessing -> Guessing(
            uiState = state,
            value = typingWord,
            onValueChanged = onValueChanged,
            isFinished = false,
            scrollState = scrollState,
        )
        is GameUiState.Loading -> Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            repeat(state.maxGuesses) { index ->
                Word()
                if (index != state.maxGuesses - 1) {
                    Spacer(modifier = Modifier.height(SpaceBetweenGuesses))
                }
            }
        }
        is GameUiState.Finished -> Finished(uiState = state)
    }
}

