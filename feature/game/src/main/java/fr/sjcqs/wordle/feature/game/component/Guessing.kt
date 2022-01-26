package fr.sjcqs.wordle.feature.game.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsHeight
import fr.sjcqs.wordle.feature.game.GameUiState
import fr.sjcqs.wordle.feature.game.GuessUiModel
import fr.sjcqs.wordle.feature.game.R
import fr.sjcqs.wordle.feature.game.SpaceBetweenGuesses
import fr.sjcqs.wordle.feature.game.StatsUiModel
import fr.sjcqs.wordle.feature.game.format
import fr.sjcqs.wordle.ui.components.Word
import fr.sjcqs.wordle.ui.icons.Icons
import fr.sjcqs.wordle.ui.theme.Shapes.TileShape
import fr.sjcqs.wordle.ui.theme.correct
import fr.sjcqs.wordle.ui.theme.onCorrect

@Composable
@OptIn(ExperimentalComposeUiApi::class)
internal fun Guessing(
    uiState: GameUiState.Guessing,
    modifier: Modifier = Modifier,
    value: String,
    onValueChanged: (String) -> Unit,
) {
    var currentValue by remember(value) { mutableStateOf(value) }

    Column(modifier = modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
        Spacer(modifier = Modifier.height(24.dp))
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
        )
        if (!uiState.isFinished) {
            Spacer(modifier = Modifier.height(16.dp))
            Keyboard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
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
        } else {
            Stats(
                modifier = Modifier
                    .padding(horizontal = 12.dp),
                stats = uiState.stats
            )
        }
        Spacer(modifier = Modifier.navigationBarsHeight())
    }
}

@Composable
private fun Stats(modifier: Modifier, stats: StatsUiModel) {
    SideEffect {
        stats.onStatsOpened()
    }
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        if (stats.expiredIn != null) {
            Column(
                modifier = Modifier.semantics(mergeDescendants = true) { },
                horizontalAlignment = CenterHorizontally
            ) {
                Text(text = stringResource(R.string.game_stats_next_word_in))
                Text(
                    text = stats.expiredIn.format(),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        if (stats.sharedText != null) {
            Button(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .semantics(mergeDescendants = true) { },
                onClick = { stats.share(stats.sharedText) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.correct,
                    contentColor = MaterialTheme.colorScheme.onCorrect,
                )
            ) {
                Icons.Share(modifier = Modifier.clearAndSetSemantics { })
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = stringResource(R.string.game_stats_share))
            }
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