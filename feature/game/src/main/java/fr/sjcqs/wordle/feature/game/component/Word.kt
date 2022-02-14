package fr.sjcqs.wordle.feature.game.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import fr.sjcqs.wordle.feature.game.model.TileUiState
import fr.sjcqs.wordle.ui.R
import fr.sjcqs.wordle.ui.components.AutoSizeText
import fr.sjcqs.wordle.ui.modifier.fade
import fr.sjcqs.wordle.ui.modifier.placeholder
import fr.sjcqs.wordle.ui.theme.Shapes.TileShape
import fr.sjcqs.wordle.ui.theme.absent
import fr.sjcqs.wordle.ui.theme.contentColorFor
import fr.sjcqs.wordle.ui.theme.correct
import fr.sjcqs.wordle.ui.theme.present


@Composable
fun Word(
    modifier: Modifier = Modifier,
    word: String = "",
    number: Int,
    tileStates: Map<Int, TileUiState> = emptyMap(),
    length: Int = 5,
    showPlaceholder: Boolean = false,
) {
    require(word.length <= length) {
        "$word is too long (max $length, current: ${word.length})"
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(length) { index ->
            val letter = word.getOrNull(index)?.toString().orEmpty()
            val tileState = tileStates[index]
            val letterStateDescription = if (letter.isEmpty()) {
                stringResource(R.string.content_description_empty)
            } else {
                stringResource(
                    id = when (tileState) {
                        TileUiState.Present -> R.string.content_description_present
                        TileUiState.Correct -> R.string.content_description_correct
                        TileUiState.Absent -> R.string.content_description_absent
                        null -> R.string.content_description_unknown
                    },
                    number,
                    index + 1
                )
            }
            val tileModifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
                .semantics(mergeDescendants = true) {
                    stateDescription = letterStateDescription
                }
            Letter(
                modifier = tileModifier,
                letterStateDescription = letterStateDescription,
                tileState = tileState,
                index = index,
                letter = letter,
                showPlaceholder = showPlaceholder
            )
            if (index + 1 < length) {
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun Letter(
    modifier: Modifier,
    letterStateDescription: String,
    tileState: TileUiState?,
    index: Int,
    letter: String,
    showPlaceholder: Boolean
) {
    AnimatedContent(
        targetState = tileState,
        modifier = modifier,
        transitionSpec = {
            when (targetState) {
                TileUiState.Present,
                TileUiState.Correct,
                TileUiState.Absent -> slideIntoContainer(
                    towards = AnimatedContentScope.SlideDirection.Up,
                    animationSpec = tween(delayMillis = index * 100)
                ) with fadeOut(targetAlpha = 1f)
                null -> hold()
            }
        }
    ) { targetState ->
        when (targetState) {
            TileUiState.Present -> BackgroundedTile(
                value = letter,
                backgroundColor = MaterialTheme.colorScheme.present,
            )
            TileUiState.Correct -> BackgroundedTile(
                value = letter,
                backgroundColor = MaterialTheme.colorScheme.correct,
            )
            TileUiState.Absent -> BackgroundedTile(
                value = letter,
                backgroundColor = MaterialTheme.colorScheme.absent,
            )
            null -> OutlinedTile(
                modifier = Modifier.placeholder(
                    visible = showPlaceholder,
                    highlight = PlaceholderHighlight.fade()
                ),
                value = letter
            )
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
    Box(
        modifier = modifier.border(
            width = 1.5.dp,
            color = outlineColor.copy(alpha = if (value.isEmpty()) 0.38f else 1f),
            shape = TileShape
        )
    ) {
        Tile(
            value = value,
            contentColor = contentColor,
        )
    }
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

    Box(modifier = modifier.fillMaxSize()) {
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

@ExperimentalAnimationApi
private fun hold(): ContentTransform = fadeIn(initialAlpha = 1f) with fadeOut(targetAlpha = 1f)