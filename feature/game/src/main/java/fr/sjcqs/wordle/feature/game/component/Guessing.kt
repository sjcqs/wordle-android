package fr.sjcqs.wordle.feature.game.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsHeight
import fr.sjcqs.wordle.feature.game.R
import fr.sjcqs.wordle.feature.game.SpaceBetweenGuesses
import fr.sjcqs.wordle.feature.game.format
import fr.sjcqs.wordle.feature.game.model.GameUiState
import fr.sjcqs.wordle.feature.game.model.GuessUiModel
import fr.sjcqs.wordle.ui.components.Word
import fr.sjcqs.wordle.ui.icons.Icons
import fr.sjcqs.wordle.ui.theme.Shapes.TileShape
import fr.sjcqs.wordle.ui.theme.correct
import fr.sjcqs.wordle.ui.theme.onCorrect
import java.time.Duration

@Composable
internal fun Guessing(
    uiState: GameUiState.Guessing,
    modifier: Modifier = Modifier,
    value: String,
    keyboardHeight: MutableState<Dp> = mutableStateOf(0.dp),
    isLoading: Boolean = false,
) {
    var currentValue by remember(value) { mutableStateOf(value) }

    val onBackspace = fun() { currentValue = currentValue.dropLast(1) }
    val onEnter = fun() {
        if (currentValue.length == uiState.word.length) {
            uiState.onSubmit(currentValue)
        }
    }
    val onCharacter = fun(character: String) {
        if (currentValue.length < uiState.word.length) {
            currentValue += character
        }

    }
    val verticalArrangement = if (uiState.isFinished) {
        Arrangement.SpaceEvenly
    } else {
        Arrangement.SpaceBetween
    }
    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = verticalArrangement
    ) {
        Guessing(
            modifier = Modifier
                .align(CenterHorizontally)
                .fillMaxWidth(0.9f),
            word = uiState.word,
            isFinished = uiState.isFinished,
            guesses = uiState.guesses,
            value = currentValue,
            onRetry = uiState.onRetry,
            canRetry = uiState.canRetry,
            isLoading = isLoading,
        )
        if (uiState.isFinished) {
            if (uiState.expiredInFlow != null) {
                val expiredIn by uiState.expiredInFlow.collectAsState()
                Footer(
                    expiredIn = expiredIn,
                    modifier = Modifier
                        .padding(horizontal = 12.dp),
                    onShare = { uiState.share(uiState.sharedText) }
                )
            }
        } else {
            val density = LocalDensity.current
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .onGloballyPositioned {
                    keyboardHeight.value = with(density) {
                        it.boundsInRoot().height.toDp() + 16.dp
                    }
                }) {
                Spacer(modifier = Modifier.height(16.dp))
                Keyboard(
                    modifier = Modifier.fillMaxWidth(),
                    layout = uiState.keyboardLayout,
                    keyStates = uiState.keyStates,
                    onKeyPressed = { keycode ->
                        when (keycode) {
                            is Keycode.Backspace -> onBackspace()
                            is Keycode.Character -> onCharacter(keycode.char)
                            is Keycode.Enter -> onEnter()
                        }
                    },
                    showPlaceholder = isLoading
                )
            }
        }
        Spacer(modifier = Modifier.navigationBarsHeight())
    }
}

@Composable
private fun Footer(
    expiredIn: Duration,
    modifier: Modifier,
    onShare: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Column(
            modifier = Modifier.semantics(mergeDescendants = true) { },
            horizontalAlignment = CenterHorizontally
        ) {
            Text(text = stringResource(R.string.game_stats_next_word_in))
            Text(
                text = expiredIn.format(),
                style = MaterialTheme.typography.headlineMedium
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.semantics(mergeDescendants = true) { },
            onClick = onShare,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.correct,
                contentColor = MaterialTheme.colorScheme.onCorrect,
            )
        ) {
            Icons.Share(modifier = Modifier.clearAndSetSemantics { })
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = stringResource(R.string.game_share_action))
        }
    }
}

@Composable
private fun Guessing(
    guesses: List<GuessUiModel>,
    modifier: Modifier,
    value: String,
    word: String,
    isFinished: Boolean,
    canRetry: Boolean,
    onRetry: () -> Unit,
    isLoading: Boolean,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = CenterHorizontally
    ) {
        if (isFinished) {
            DailyWord(word)
            Spacer(modifier = Modifier.height(24.dp))
        }
        guesses.forEachIndexed { index, guess ->
            Word(
                word = if (guess.isEditable) value else guess.word,
                number = index + 1,
                tileStates = if (guess.isEditable) emptyMap() else guess.tileState,
                showPlaceholder = isLoading
            )
            if (index < guesses.lastIndex) {
                Spacer(modifier = Modifier.height(SpaceBetweenGuesses))
            }
        }
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