package fr.sjcqs.wordle.feature.game.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.sjcqs.wordle.feature.game.GameUiState
import fr.sjcqs.wordle.feature.game.GuessUiModel
import fr.sjcqs.wordle.ui.components.TileUiState
import fr.sjcqs.wordle.ui.theme.Shapes.TileShape
import fr.sjcqs.wordle.ui.theme.WordleTheme
import fr.sjcqs.wordle.ui.theme.absent
import fr.sjcqs.wordle.ui.theme.correct
import fr.sjcqs.wordle.ui.theme.present

@Composable
internal fun Finished(uiState: GameUiState.Finished, ) {
    Finished(
        word = uiState.word,
        guesses = uiState.guesses,
        isWon = uiState.isWon,
        length = uiState.length,
    )
}

@Composable
private fun Finished(
    word: String,
    guesses: List<GuessUiModel>,
    isWon: Boolean,
    length: Int,
) {
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
    ) {
        val tileSize = 48.dp
        guesses.forEach { guess ->
            Tiles(guess, tileSize)
            Spacer(modifier = Modifier.height(4.dp))
        }
        Word(word, tileSize)
    }
}

@Composable
private fun Word(word: String, tileSize: Dp) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        word.forEachIndexed { index, letter ->
            Box(modifier = Modifier.size(tileSize)) {
                Text(
                    text = letter.toString(),
                    modifier = Modifier
                        .align(Center),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            if (index != word.lastIndex) {
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

@Composable
private fun Tiles(
    guess: GuessUiModel,
    tileSize: Dp,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val tiles = guess.tileState.values.toList()
        tiles.forEachIndexed { index, tile ->
            Surface(
                color = when (tile) {
                    TileUiState.Present -> MaterialTheme.colorScheme.present
                    TileUiState.HintCorrect,
                    TileUiState.Correct,
                    -> MaterialTheme.colorScheme.correct
                    TileUiState.HintAbsent,
                    TileUiState.Absent,
                    -> MaterialTheme.colorScheme.absent
                },
                shape = TileShape,
                shadowElevation = 1.5.dp,
                modifier = Modifier.size(tileSize),
                content = {}
            )
            if (index != tiles.lastIndex) {
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

@Preview
@Composable
private fun FinishedPreview() {
    WordleTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(vertical = 8.dp)
        ) {
            Finished(uiState = GameUiState.Finished(
                "MELEE",
                isWon = false,
                guesses = listOf(
                    GuessUiModel(tileState = mapOf(
                        0 to TileUiState.values().random(),
                        1 to TileUiState.values().random(),
                        2 to TileUiState.values().random(),
                        3 to TileUiState.values().random(),
                        4 to TileUiState.values().random(),
                    )),
                    GuessUiModel(tileState = mapOf(
                        0 to TileUiState.values().random(),
                        1 to TileUiState.values().random(),
                        2 to TileUiState.values().random(),
                        3 to TileUiState.values().random(),
                        4 to TileUiState.values().random(),
                    )),
                    GuessUiModel(tileState = mapOf(
                        0 to TileUiState.values().random(),
                        1 to TileUiState.values().random(),
                        2 to TileUiState.values().random(),
                        3 to TileUiState.values().random(),
                        4 to TileUiState.values().random(),
                    )),
                ),
                length = 5
            ))
        }
    }
}