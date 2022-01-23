package fr.sjcqs.wordle.feature.game.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.platform.TextToolbarStatus
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.derivedWindowInsetsTypeOf
import fr.sjcqs.wordle.feature.game.GameUiState
import fr.sjcqs.wordle.feature.game.GuessUiModel
import fr.sjcqs.wordle.feature.game.SpaceBetweenGuesses
import fr.sjcqs.wordle.ui.components.TileUiState
import fr.sjcqs.wordle.ui.components.Word
import fr.sjcqs.wordle.ui.theme.Shapes.TileShape
import java.util.Locale

@Composable
@OptIn(ExperimentalComposeUiApi::class)
internal fun Guessing(
    uiState: GameUiState.Guessing,
    value: String,
    onValueChanged: (String) -> Unit,
    scrollState: ScrollState,
) {
    val ime = LocalWindowInsets.current.ime
    val navBars = LocalWindowInsets.current.navigationBars
    val insets = remember(ime, navBars) { derivedWindowInsetsTypeOf(ime, navBars) }
    val navigationWithImeBottom = with(LocalDensity.current) { insets.bottom.toDp() }

    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    var currentValue by remember(value) { mutableStateOf(value) }
    val currentTiles: Map<Int, TileUiState> by derivedStateOf {
        currentValue.mapIndexedNotNull { index, letter ->
            val tileState = uiState.tilesLetters[index]?.get(letter)
            if (tileState != null) (index to tileState) else null
        }.toMap()
    }

    Column {
        Guessing(
            word = uiState.word,
            isFinished = uiState.isFinished,
            guesses = uiState.guesses,
            value = currentValue,
            currentTiles = currentTiles,
            isImeVisible = ime.isVisible,
            openKeyboard = {
                if (!uiState.isFinished) {
                    focusRequester.requestFocus()
                    keyboard?.show()
                }
            },
            onRetry = uiState.onRetry,
            canRetry = uiState.canRetry,
            navigationWithImeBottom = navigationWithImeBottom,
            scrollState = scrollState
        )
        if (!uiState.isFinished) {
            LaunchedEffect(key1 = Unit) {
                focusRequester.requestFocus()
            }
            CompositionLocalProvider(LocalTextToolbar provides NoOpTextToolbar) {
                TextField(
                    value = currentValue,
                    singleLine = true,
                    onValueChange = { newValue ->
                        if (newValue.isNotEmpty()) {
                            uiState.onTyping()
                        }
                        val filteredNewValue =
                            newValue.uppercase(Locale.FRANCE).filter { it in 'A'..'Z' }
                        if (filteredNewValue.length <= uiState.word.length) {
                            currentValue = filteredNewValue
                            onValueChanged(filteredNewValue)
                        }
                    },
                    modifier = Modifier
                        .size(0.dp)
                        .alpha(0f)
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Characters,
                        autoCorrect = false,
                        imeAction = if (currentValue.length == uiState.word.length) {
                            ImeAction.Done
                        } else {
                            ImeAction.None
                        }
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        uiState.onSubmit(currentValue)
                    })
                )
            }
        }
    }
}

object NoOpTextToolbar : TextToolbar {
    override val status: TextToolbarStatus = TextToolbarStatus.Hidden

    override fun hide() {
        /* no-op */
    }

    override fun showMenu(
        rect: Rect,
        onCopyRequested: (() -> Unit)?,
        onPasteRequested: (() -> Unit)?,
        onCutRequested: (() -> Unit)?,
        onSelectAllRequested: (() -> Unit)?
    ) {
        /* no-op */
    }

}

@Composable
private fun Guessing(
    guesses: List<GuessUiModel>,
    value: String,
    currentTiles: Map<Int, TileUiState>,
    isImeVisible: Boolean,
    openKeyboard: () -> Unit,
    navigationWithImeBottom: Dp,
    scrollState: ScrollState,
    word: String,
    isFinished: Boolean,
    canRetry: Boolean,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val density = LocalDensity.current
        var currentTileOffset: Dp by remember { mutableStateOf(0.dp) }
        val scrollBy: Dp by derivedStateOf { currentTileOffset + 16.dp }
        LaunchedEffect(key1 = scrollBy) {
            if (isImeVisible) {
                scrollState.scrollBy(scrollBy.value)
            }
        }
        if (isFinished) {
            DailyWord(word)
            Spacer(modifier = Modifier.height(24.dp))
        }
        guesses.forEachIndexed { index, guess ->
            Guess(
                word = if (guess.isEditable) value else guess.word,
                modifier = if (guess.isEditable && isImeVisible) {
                    Modifier.onGloballyPositioned { coordinates ->
                        with(density) {
                            val parentHeight = (coordinates.parentCoordinates?.size?.height)?.toDp()
                                ?: 0.dp
                            val bottom = coordinates.positionInWindow().y.toDp()
                            val offset = bottom - (parentHeight - navigationWithImeBottom)
                            if (offset > 0.dp) {
                                currentTileOffset = offset
                            }
                        }
                    }
                } else Modifier,
                tiles = if (guess.isEditable) currentTiles else guess.tileState,
                onTileClicked = openKeyboard,
                number = index + 1
            )
            if (index < guesses.lastIndex) {
                Spacer(modifier = Modifier.height(SpaceBetweenGuesses))
            }
        }
        Spacer(modifier = Modifier.height(navigationWithImeBottom))
    }
}

@Composable
private fun DailyWord(word: String) {
    Surface(
        color = MaterialTheme.colorScheme.inverseSurface,
        shape = TileShape
    ) {
        Text(
            text = word,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun Guess(
    word: String,
    number: Int,
    modifier: Modifier,
    onTileClicked: () -> Unit,
    tiles: Map<Int, TileUiState>,
) {
    Word(
        modifier = modifier,
        word = word,
        number = number,
        onTileClicked = onTileClicked,
        tileStates = tiles
    )
}