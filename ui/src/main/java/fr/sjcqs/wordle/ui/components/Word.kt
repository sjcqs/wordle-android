package fr.sjcqs.wordle.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.with
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalContentColor
import androidx.compose.material.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import fr.sjcqs.wordle.ui.theme.absent
import fr.sjcqs.wordle.ui.theme.contentColorFor
import fr.sjcqs.wordle.ui.theme.correct
import fr.sjcqs.wordle.ui.theme.present

enum class TileUiState { Present, Correct, Absent }

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Word(
    value: String,
    modifier: Modifier = Modifier,
    tileStates: Map<Int, TileUiState> = emptyMap(),
    length: Int = 5,
    isEditable: Boolean = true,
    onSubmit: (word: String) -> Unit
) {
    require(value.length <= length) {
        "$value is too long (max $length, current: ${value.length})"
    }
    var currentValue by remember { mutableStateOf(value.uppercase()) }
    val focusRequester = remember { FocusRequester() }
    val imeAction by derivedStateOf {
        if (currentValue.length == length) ImeAction.Done else ImeAction.None
    }
    val keyboard = LocalSoftwareKeyboardController.current

    if (isEditable) {
        DisposableEffect(Unit) {
            focusRequester.requestFocus()
            onDispose {
                /* no-op */
            }
        }
    }
    TextField(
        value = currentValue,
        readOnly = !isEditable,
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

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        val tileModifier = Modifier
            .weight(1f)
            .aspectRatio(1f)
            .run {
                if (isEditable) {
                    clickable {
                        focusRequester.requestFocus()
                        keyboard?.show()
                    }
                } else this
            }
        repeat(length) { index ->
            val letter = currentValue.getOrNull(index)?.toString().orEmpty()
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
                null -> OutlinedTile(
                    value = letter,
                    modifier = tileModifier,
                    outlineColor = MaterialTheme.colorScheme.outline
                )
            }
            if (index + 1 < length) {
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
    }
}

private val TileShape
    @Composable
    get() = RoundedCornerShape(2.dp)

@Composable
private fun OutlinedTile(
    value: String,
    outlineColor: Color,
    modifier: Modifier = Modifier,
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
@OptIn(ExperimentalAnimationApi::class)
private fun Tile(
    value: String,
    modifier: Modifier = Modifier,
    contentColor: Color = LocalContentColor.current,
) {
    Box(modifier = modifier) {
        AnimatedContent(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(8.dp),
            targetState = value,
            transitionSpec = {
                if (targetState.isEmpty()) {
                    expandIn { size -> size } + fadeIn() with
                            shrinkOut { IntSize.Zero } + fadeOut()
                } else {
                    expandIn { IntSize.Zero } + fadeIn() with
                            shrinkOut { size -> size } + fadeOut()
                }.using(SizeTransform(clip = false))
            }
        ) { targetValue ->
            AutoSizeText(
                text = targetValue,
                style = MaterialTheme.typography.labelLarge,
                color = contentColor,
            )
        }
    }
}