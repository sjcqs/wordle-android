package fr.sjcqs.wordle.feature.game.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.sjcqs.wordle.feature.game.GameUiState
import fr.sjcqs.wordle.feature.game.GuessUiModel
import fr.sjcqs.wordle.feature.game.SpaceBetweenGuesses
import fr.sjcqs.wordle.ui.components.Word
import fr.sjcqs.wordle.ui.theme.Shapes.TileShape

@Composable
@OptIn(ExperimentalComposeUiApi::class)
internal fun Guessing(
    uiState: GameUiState.Guessing,
    modifier: Modifier = Modifier,
    value: String,
    onValueChanged: (String) -> Unit,
) {
    var currentValue by remember(value) { mutableStateOf(value) }

    Column(modifier = modifier, verticalArrangement = Arrangement.Top) {
        Spacer(modifier = Modifier.height(24.dp))
        Guessing(
            modifier = Modifier
                .align(CenterHorizontally)
                .fillMaxWidth(0.8f),
            word = uiState.word,
            isFinished = uiState.isFinished,
            guesses = uiState.guesses,
            value = currentValue,
            onRetry = uiState.onRetry,
            canRetry = uiState.canRetry,
        )
        if (!uiState.isFinished) {
            Spacer(modifier = Modifier.weight(1f))
            Keyboard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                keyStates = uiState.keyStates,
                onKeyPressed = { keycode ->
                    when (keycode) {
                        Keycode.Backspace -> {
                            currentValue = currentValue.dropLast(1)
                            onValueChanged(currentValue)
                        }
                        is Keycode.Character -> {
                            if (currentValue.length < uiState.word.length) {
                                currentValue += keycode.char
                                onValueChanged(currentValue)
                            }
                        }
                        Keycode.Enter -> {
                            if (currentValue.length == uiState.word.length) {
                                uiState.onSubmit(currentValue)
                            }
                        }
                    }
                })
            Spacer(modifier = Modifier.weight(1f))
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
                tileStates = if (guess.isEditable) emptyMap() else guess.tileState,
                number = index + 1
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