package fr.sjcqs.wordle.feature.guessing

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.derivedWindowInsetsTypeOf
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.insets.ui.Scaffold
import fr.sjcqs.wordle.logger.LocalLogger
import fr.sjcqs.wordle.ui.components.TileUiState
import fr.sjcqs.wordle.ui.components.Word


@Composable
fun Guessing() {
    val viewModel: GuessingViewModel = hiltViewModel()
    val uiState by viewModel.uiStateFlow.collectAsState()
    val ime = LocalWindowInsets.current.ime
    val elevation: Dp by remember { derivedStateOf { if (ime.isVisible) 4.dp else 0.dp } }
    Scaffold(
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
                        text = "Le Mot",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.headlineLarge,
                    )
                }
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Guessing(uiState = uiState) { viewModel.onSubmit(it) }
            }
        })
}

@Composable
private fun Guessing(uiState: GuessingUiState, onSubmit: (word: String) -> Unit) {
    LocalLogger.current.d(uiState.toString())
    when (uiState) {
        is GuessingUiState.Guessing -> Guessing(
            uiModel = uiState,
            onSubmit = onSubmit
        )
        GuessingUiState.Loading -> CircularProgressIndicator()
    }
}

@Composable
private fun Guessing(
    uiModel: GuessingUiState.Guessing,
    onSubmit: (word: String) -> Unit
) {

    Guessing(
        guesses = uiModel.guesses,
        length = uiModel.length,
        isFinished = uiModel.isFinished,
        onSubmit = onSubmit
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Guessing(
    guesses: List<GuessUiModel>,
    length: Int,
    isFinished: Boolean,
    onSubmit: (word: String) -> Unit,
) {
    val ime = LocalWindowInsets.current.ime
    val navBars = LocalWindowInsets.current.navigationBars
    val insets = remember(ime, navBars) { derivedWindowInsetsTypeOf(ime, navBars) }
    val navigationWithImeBottom = with(LocalDensity.current) { insets.bottom.toDp() }
    val scrollState = rememberScrollState()

    var currentValue by remember(guesses) { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val imeAction by derivedStateOf {
        if (currentValue.length == length) ImeAction.Done else ImeAction.None
    }
    val keyboard = LocalSoftwareKeyboardController.current

    val openKeyboard =  {
        if (!isFinished) {
            keyboard?.show()
            focusRequester.requestFocus()
        }
    }

    if (!isFinished) {
        DisposableEffect(Unit) {
            focusRequester.requestFocus()
            onDispose {
                /* no-op */
            }
        }
        TextField(
            value = currentValue,
            singleLine = true,
            onValueChange = { newValue ->
                val filteredNewValue = newValue.filter { it.isLetter() }
                if (filteredNewValue.length <= length) {
                    currentValue = filteredNewValue
                }
            },
            modifier = Modifier
                .size(0.dp)
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Ascii,
                capitalization = KeyboardCapitalization.Characters,
                autoCorrect = false,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(onDone = { onSubmit(currentValue) })
        )
    }

    Column(
        modifier = Modifier
            .verticalScroll(scrollState, enabled = false)
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val density = LocalDensity.current
        var scrollBy: Dp by remember { mutableStateOf(0.dp) }
        LaunchedEffect(key1 = scrollBy) {
            if (ime.isVisible) {
                scrollState.scrollBy(scrollBy.value)
            }
        }
        guesses.forEachIndexed { index, guess ->
            Guess(
                word = if (guess.isEditable) currentValue else guess.word,
                modifier = if (guess.isEditable) {
                    Modifier
                        .onGloballyPositioned { coordinates ->
                            val parentHeight = with(density) {
                                (coordinates.parentCoordinates?.size?.height ?: 0).toDp()
                            }
                            val bottom = with(density) { coordinates.positionInWindow().y.toDp() }
                            val offset = bottom - (parentHeight - navigationWithImeBottom)
                            if (offset > 0.dp) {
                                scrollBy = offset + 16.dp
                            }
                        }
                } else Modifier,
                tiles = guess.tileState,
                onSubmit = onSubmit,
                onTileClicked = openKeyboard
            )
            if (index < guesses.lastIndex) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Spacer(modifier = Modifier.height(navigationWithImeBottom))
    }
}

@Composable
private fun Guess(
    word: String,
    modifier: Modifier,
    onTileClicked: () -> Unit,
    tiles: Map<Int, TileUiState>,
    onSubmit: (word: String) -> Unit
) {
    Word(
        modifier = modifier,
        word = word,
        onTileClicked = onTileClicked,
        onSubmit = onSubmit,
        tileStates = tiles
    )
}
