package fr.sjcqs.wordle.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.sjcqs.wordle.ui.theme.Shapes.TileShape
import fr.sjcqs.wordle.ui.theme.absent
import fr.sjcqs.wordle.ui.theme.contentColorFor
import fr.sjcqs.wordle.ui.theme.correct
import fr.sjcqs.wordle.ui.theme.present

enum class TileUiState {
    Present,
    Correct,
    Absent,
    HintCorrect,
    HintAbsent,
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Word(
    modifier: Modifier = Modifier,
    word: String = "",
    tileStates: Map<Int, TileUiState> = emptyMap(),
    length: Int = 5,
    onTileClicked: () -> Unit,
) {
    require(word.length <= length) {
        "$word is too long (max $length, current: ${word.length})"
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        val tileModifier = Modifier
            .weight(1f)
            .aspectRatio(1f)
            .clickable {
                onTileClicked()
            }
        repeat(length) { index ->
            val letter = word.getOrNull(index)?.toString().orEmpty()
            when (tileStates[index]) {
                TileUiState.Present -> BackgroundedTile(
                    value = letter,
                    modifier = tileModifier,
                    backgroundColor = MaterialTheme.colorScheme.present,
                )
                TileUiState.Correct -> BackgroundedTile(
                    value = letter,
                    modifier = tileModifier,
                    backgroundColor = MaterialTheme.colorScheme.correct,
                )
                TileUiState.Absent -> BackgroundedTile(
                    value = letter,
                    modifier = tileModifier,
                    backgroundColor = MaterialTheme.colorScheme.absent,
                )
                TileUiState.HintCorrect -> OutlinedTile(
                    value = letter,
                    modifier = tileModifier,
                    contentColor = MaterialTheme.colorScheme.correct,
                    outlineColor = MaterialTheme.colorScheme.correct
                )
                TileUiState.HintAbsent -> OutlinedTile(
                    value = letter,
                    modifier = tileModifier,
                    contentColor = MaterialTheme.colorScheme.absent,
                    outlineColor = MaterialTheme.colorScheme.absent
                )
                null -> OutlinedTile(
                    value = letter,
                    modifier = tileModifier,
                )
            }
            if (index + 1 < length) {
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
    }
}

@Composable
private fun OutlinedTile(
    value: String,
    modifier: Modifier = Modifier,
    outlineColor: Color = MaterialTheme.colorScheme.outline,
    contentColor: Color = LocalContentColor.current,
) {
    Tile(
        value = value,
        contentColor = contentColor,
        modifier = modifier.border(width = 1.5.dp, outlineColor, TileShape),
    )
}

@Composable
private fun BackgroundedTile(
    value: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    contentColor: Color = contentColorFor(backgroundColor = backgroundColor),
) {
    Surface(
        modifier,
        shadowElevation = 2.dp,
        color = backgroundColor,
        shape = TileShape
    ) {
        Tile(
            value = value,
            contentColor = contentColor,
        )
    }
}

@Composable
private fun Tile(
    value: String,
    modifier: Modifier = Modifier,
    contentColor: Color = LocalContentColor.current,
) {
    Box(modifier = modifier) {
        AutoSizeText(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(8.dp),
            text = value,
            style = MaterialTheme.typography.labelLarge,
            color = contentColor,
        )
    }
}