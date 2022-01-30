package fr.sjcqs.wordle.feature.game.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import com.google.accompanist.placeholder.PlaceholderHighlight
import fr.sjcqs.wordle.haptics.LocalHapticController
import fr.sjcqs.wordle.haptics.tick
import fr.sjcqs.wordle.ui.components.TileUiState
import fr.sjcqs.wordle.ui.icons.Icons
import fr.sjcqs.wordle.ui.modifier.fade
import fr.sjcqs.wordle.ui.modifier.placeholder
import fr.sjcqs.wordle.ui.theme.WordleTheme
import fr.sjcqs.wordle.ui.theme.absent
import fr.sjcqs.wordle.ui.theme.contentColorFor
import fr.sjcqs.wordle.ui.theme.correct
import fr.sjcqs.wordle.ui.theme.present

internal sealed interface Keycode {
    object Backspace : Keycode
    object Enter : Keycode
    data class Character(val char: String) : Keycode
}

internal typealias Keys = List<Pair<Keycode, Float>>

internal enum class KeyboardLayoutUiModel(
    private val firstRow: Keys,
    private val secondRow: Keys,
    private val thirdRow: Keys,
) : List<Keys> by listOf(firstRow, secondRow, thirdRow) {
    Azerty(
        listOf(
            Keycode.Character("A") to 1f,
            Keycode.Character("Z") to 1f,
            Keycode.Character("E") to 1f,
            Keycode.Character("R") to 1f,
            Keycode.Character("T") to 1f,
            Keycode.Character("Y") to 1f,
            Keycode.Character("U") to 1f,
            Keycode.Character("I") to 1f,
            Keycode.Character("O") to 1f,
            Keycode.Character("P") to 1f,
        ),
        listOf(
            Keycode.Character("Q") to 1f,
            Keycode.Character("S") to 1f,
            Keycode.Character("D") to 1f,
            Keycode.Character("F") to 1f,
            Keycode.Character("G") to 1f,
            Keycode.Character("H") to 1f,
            Keycode.Character("J") to 1f,
            Keycode.Character("K") to 1f,
            Keycode.Character("L") to 1f,
            Keycode.Character("M") to 1f,
        ),
        listOf(
            Keycode.Enter to 1f,
            Keycode.Character("W") to 0.5f,
            Keycode.Character("X") to 0.5f,
            Keycode.Character("C") to 0.5f,
            Keycode.Character("V") to 0.5f,
            Keycode.Character("B") to 0.5f,
            Keycode.Character("N") to 0.5f,
            Keycode.Backspace to 1f,
        ),
    ),
    Qwerty(
        listOf(
            Keycode.Character("Q") to 1f,
            Keycode.Character("W") to 1f,
            Keycode.Character("E") to 1f,
            Keycode.Character("R") to 1f,
            Keycode.Character("T") to 1f,
            Keycode.Character("Y") to 1f,
            Keycode.Character("U") to 1f,
            Keycode.Character("I") to 1f,
            Keycode.Character("O") to 1f,
            Keycode.Character("P") to 1f,
        ),
        listOf(
            Keycode.Character("A") to 1f,
            Keycode.Character("S") to 1f,
            Keycode.Character("D") to 1f,
            Keycode.Character("F") to 1f,
            Keycode.Character("G") to 1f,
            Keycode.Character("H") to 1f,
            Keycode.Character("J") to 1f,
            Keycode.Character("K") to 1f,
            Keycode.Character("L") to 1f,
            Keycode.Character("M") to 1f,
        ),
        listOf(
            Keycode.Enter to 1f,
            Keycode.Character("Z") to 0.5f,
            Keycode.Character("X") to 0.5f,
            Keycode.Character("C") to 0.5f,
            Keycode.Character("V") to 0.5f,
            Keycode.Character("B") to 0.5f,
            Keycode.Character("N") to 0.5f,
            Keycode.Backspace to 1f,
        ),
    )
}

@Composable
internal fun Keyboard(
    modifier: Modifier = Modifier,
    layout: KeyboardLayoutUiModel = KeyboardLayoutUiModel.Azerty,
    keyStates: Map<String, TileUiState> = emptyMap(),
    onKeyPressed: (key: Keycode) -> Unit,
    showPlaceholder: Boolean = false
) {
    val hapticsController = LocalHapticController.current
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        layout.forEach { row ->
            Row(modifier = Modifier.width(IntrinsicSize.Min)) {
                row.forEach { (keycode, weight) ->
                    val keyState = if (keycode is Keycode.Character) {
                        keyStates[keycode.char]
                    } else {
                        null
                    }
                    Key(
                        keycode = keycode,
                        onPressed = {
                            hapticsController.tick()
                            onKeyPressed(keycode)
                        },
                        keyState = keyState,
                        modifier = Modifier
                            .weight(weight)
                            .width(IntrinsicSize.Min)
                            .height(IntrinsicSize.Min)
                            .padding(horizontal = 2.dp, vertical = 4.dp)
                            .placeholder(showPlaceholder, highlight = PlaceholderHighlight.fade())
                    )
                }
            }
        }
    }
}

@Composable
private fun Key(
    keycode: Keycode,
    onPressed: () -> Unit,
    modifier: Modifier,
    keyState: TileUiState?
) {
    val color by animateColorAsState(
        targetValue = when (keyState) {
            TileUiState.Present -> MaterialTheme.colorScheme.present
            TileUiState.Correct -> MaterialTheme.colorScheme.correct
            TileUiState.Absent -> MaterialTheme.colorScheme.absent
            null -> MaterialTheme.colorScheme.surface
        },
        animationSpec = tween()
    )
    val contentColor = contentColorFor(backgroundColor = color)
    val interactionSource = remember { MutableInteractionSource() }
    val showTooltip by interactionSource.collectIsPressedAsState()
    Box(modifier = modifier) {
        if (keycode is Keycode.Character && showTooltip) {
            KeyTooltip(keycode)
        }
        Surface(
            onClick = onPressed,
            color = color,
            interactionSource = interactionSource,
            contentColor = contentColor,
            shape = RoundedCornerShape(4.dp),
            shadowElevation = 4.dp,
            role = Role.Button
        ) {
            Content(
                modifier = Modifier.fillMaxSize(),
                keycode = keycode,
            )
        }
    }
}

@Composable
private fun KeyTooltip(keycode: Keycode.Character) {
    Popup(
        popupPositionProvider = object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize
            ): IntOffset {
                return IntOffset(
                    anchorBounds.left + anchorBounds.width / 2 - popupContentSize.width / 2,
                    anchorBounds.top - anchorBounds.height
                )
            }

        },
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Content(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 12.dp)
                    .align(Alignment.Center),
                keycode = keycode,
                textStyle = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
private fun Content(
    keycode: Keycode,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.headlineSmall
) {
    Box(modifier = modifier) {
        when (keycode) {
            Keycode.Backspace -> {
                Icons.Backspace(modifier = Modifier.align(Alignment.Center))
            }
            is Keycode.Character -> {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = keycode.char,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    style = textStyle
                )
            }
            Keycode.Enter -> {
                Icons.Enter(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

private class BooleanPreviewParameterProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean>
        get() = sequenceOf(true, false)

}

@Preview
@Composable
internal fun KeyboardPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) showPlaceholder: Boolean
) {
    WordleTheme {
        Keyboard(
            modifier = Modifier.fillMaxWidth(),
            keyStates = mutableMapOf(
                "A" to TileUiState.Present,
                "D" to TileUiState.Correct,
                "E" to TileUiState.Absent,
                "H" to TileUiState.Absent,
                "J" to TileUiState.Absent,
            ),
            onKeyPressed = {},
            showPlaceholder = showPlaceholder
        )
    }
}

@Preview
@Composable
internal fun KeyboardPreviewDark(
    @PreviewParameter(BooleanPreviewParameterProvider::class) showPlaceholder: Boolean
) {
    WordleTheme(true) {
        Keyboard(
            modifier = Modifier.fillMaxWidth(),
            keyStates = mutableMapOf(
                "A" to TileUiState.Present,
                "D" to TileUiState.Correct,
                "E" to TileUiState.Absent,
                "H" to TileUiState.Absent,
                "J" to TileUiState.Absent,
            ),
            onKeyPressed = {},
            showPlaceholder = showPlaceholder
        )
    }
}