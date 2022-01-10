package fr.sjcqs.wordle.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material3.MaterialTheme
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

enum class LetterUiState { Correct, Absent, Present }

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Word(
    value: String,
    modifier: Modifier = Modifier,
    letterStates: Map<Int, LetterUiState> = emptyMap(),
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
            onDispose { }
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
        repeat(length) { index ->
            val letter = currentValue.getOrNull(index)
            val state = letterStates[index]
            Letter(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .run {
                        if (isEditable) {
                            clickable {
                                focusRequester.requestFocus()
                                keyboard?.show()
                            }
                        } else this
                    },
                value = letter,
                state = state,
            )
            if (index + 1 < length) {
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
    }
}

@Composable
@OptIn(ExperimentalAnimationApi::class)
fun Letter(
    value: Char?,
    state: LetterUiState?,
    modifier: Modifier = Modifier,
) {
    val stateModifier = when (state) {
        LetterUiState.Present -> Modifier.background(MaterialTheme.colorScheme.present)
        LetterUiState.Correct -> Modifier.background(MaterialTheme.colorScheme.correct)
        LetterUiState.Absent -> Modifier.background(MaterialTheme.colorScheme.absent)
        null -> Modifier.border(1.5.dp, MaterialTheme.colorScheme.outline)
    }
    val color = contentColorFor(
        backgroundColor = when (state) {
            LetterUiState.Present -> MaterialTheme.colorScheme.present
            LetterUiState.Correct -> MaterialTheme.colorScheme.correct
            LetterUiState.Absent -> MaterialTheme.colorScheme.absent
            null -> MaterialTheme.colorScheme.background
        }
    )
    Box(modifier = modifier.then(stateModifier)) {
        AnimatedContent(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(8.dp),
            targetState = value,
            transitionSpec = {
                if (targetState != null) {
                    expandIn { size -> size } + fadeIn() with
                            shrinkOut { IntSize.Zero } + fadeOut()
                } else {
                    expandIn { IntSize.Zero } + fadeIn() with
                            shrinkOut { size -> size } + fadeOut()
                }.using(SizeTransform(clip = false))
            }
        ) { targetValue ->
            AutoSizeText(
                text = targetValue?.toString().orEmpty(),
                style = MaterialTheme.typography.labelLarge,
                color = color,
            )
        }
    }
}