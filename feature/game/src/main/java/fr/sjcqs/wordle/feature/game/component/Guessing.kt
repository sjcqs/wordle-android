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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.derivedWindowInsetsTypeOf
import fr.sjcqs.wordle.feature.game.GameUiState
import fr.sjcqs.wordle.feature.game.GuessUiModel
import fr.sjcqs.wordle.ui.components.TileUiState
import fr.sjcqs.wordle.ui.components.Word

@Composable
@OptIn(ExperimentalComposeUiApi::class)
internal fun Guessing(
    uiModel: GameUiState.Guessing,
    value: String,
    onValueChanged: (String) -> Unit,
    onSubmit: (word: String) -> Unit,
    isFinished: Boolean,
    scrollState: ScrollState,
) {
    val ime = LocalWindowInsets.current.ime
    val navBars = LocalWindowInsets.current.navigationBars
    val insets = remember(ime, navBars) { derivedWindowInsetsTypeOf(ime, navBars) }
    val navigationWithImeBottom = with(LocalDensity.current) { insets.bottom.toDp() }

    val focusRequester = remember { FocusRequester() }
    var currentValue by remember(value) { mutableStateOf(value) }
    val currentTiles: Map<Int, TileUiState> by derivedStateOf {
        currentValue.mapIndexedNotNull { index, letter ->
            val tileState = uiModel.tilesLetters[index]?.get(letter)
            if (tileState != null) (index to tileState) else null
        }.toMap()
    }
    val imeAction by derivedStateOf {
        if (currentValue.length == uiModel.length) ImeAction.Done else ImeAction.None
    }
    val keyboard = LocalSoftwareKeyboardController.current

    val openKeyboard = {
        if (!ime.isVisible) {
            keyboard?.show()
            focusRequester.requestFocus()
        }
    }

    Guessing(
        guesses = uiModel.guesses,
        value = currentValue,
        currentTiles = currentTiles,
        isImeVisible = ime.isVisible,
        openKeyboard = openKeyboard,
        onSubmit = onSubmit,
        navigationWithImeBottom = navigationWithImeBottom,
        scrollState = scrollState
    )
    if (!isFinished) {
        DisposableEffect(uiModel) {
            openKeyboard()
            onDispose {
                /* no-op */
            }
        }
        TextField(
            value = currentValue,
            singleLine = true,
            onValueChange = { newValue ->
                val filteredNewValue = newValue.filter {
                    it in 'A'..'Z'
                }
                if (filteredNewValue.length <= uiModel.length) {
                    currentValue = filteredNewValue
                    onValueChanged(filteredNewValue)
                }
            },
            modifier = Modifier
                .size(0.dp)
                .alpha(0f)
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
}

@Composable
private fun Guessing(
    guesses: List<GuessUiModel>,
    value: String,
    currentTiles: Map<Int, TileUiState>,
    isImeVisible: Boolean,
    openKeyboard: () -> Unit,
    onSubmit: (word: String) -> Unit,
    navigationWithImeBottom: Dp,
    scrollState: ScrollState,
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
        LaunchedEffect(key1 = scrollBy, key2 = isImeVisible) {
            if (isImeVisible) {
                scrollState.scrollBy(scrollBy.value)
            }
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
    onSubmit: (word: String) -> Unit,
) {
    Word(
        modifier = modifier,
        word = word,
        onTileClicked = onTileClicked,
        onSubmit = onSubmit,
        tileStates = tiles
    )
}