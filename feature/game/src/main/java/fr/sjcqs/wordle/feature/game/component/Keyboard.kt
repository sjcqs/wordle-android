package fr.sjcqs.wordle.feature.game.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.sjcqs.wordle.haptics.LocalHapticController
import fr.sjcqs.wordle.haptics.tick
import fr.sjcqs.wordle.ui.components.TileUiState
import fr.sjcqs.wordle.ui.icons.Icons
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

@Composable
internal fun Keyboard(
    modifier: Modifier = Modifier,
    keyStates: Map<String, TileUiState> = emptyMap(),
    onKeyPressed: (key: Keycode) -> Unit
) {
    val hapticsController = LocalHapticController.current
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.width(IntrinsicSize.Min)) {
            listOf(
                Keycode.Character("A"),
                Keycode.Character("Z"),
                Keycode.Character("E"),
                Keycode.Character("R"),
                Keycode.Character("T"),
                Keycode.Character("Y"),
                Keycode.Character("U"),
                Keycode.Character("I"),
                Keycode.Character("O"),
                Keycode.Character("P"),
            ).forEach { keycode ->
                Key(
                    keycode = keycode,
                    onPressed = {
                        hapticsController.tick()
                        onKeyPressed(keycode)
                    },
                    keyState = keyStates[keycode.char],
                    modifier = Modifier
                        .weight(1f)
                        .width(IntrinsicSize.Min)
                        .height(IntrinsicSize.Min)
                )
            }
        }
        Row(modifier = Modifier.width(IntrinsicSize.Min)) {
            listOf(
                Keycode.Character("Q"),
                Keycode.Character("S"),
                Keycode.Character("D"),
                Keycode.Character("F"),
                Keycode.Character("G"),
                Keycode.Character("H"),
                Keycode.Character("J"),
                Keycode.Character("K"),
                Keycode.Character("L"),
                Keycode.Character("M"),
            ).forEach { keycode ->
                Key(
                    keycode = keycode,
                    onPressed = {
                        hapticsController.tick()
                        onKeyPressed(keycode)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .width(IntrinsicSize.Min)
                        .height(IntrinsicSize.Min),
                    keyState = keyStates[keycode.char],
                )
            }
        }
        Row(modifier = Modifier.width(IntrinsicSize.Min)) {
            listOf(
                Keycode.Enter to 1f,
                Keycode.Character("W") to 0.5f,
                Keycode.Character("X") to 0.5f,
                Keycode.Character("C") to 0.5f,
                Keycode.Character("V") to 0.5f,
                Keycode.Character("B") to 0.5f,
                Keycode.Character("N") to 0.5f,
                Keycode.Backspace to 1f,
            ).forEach { (keycode, weight) ->
                Key(
                    keycode = keycode,
                    onPressed = {
                        hapticsController.tick()
                        onKeyPressed(keycode)
                    },
                    modifier = Modifier
                        .weight(weight)
                        .width(IntrinsicSize.Min)
                        .height(IntrinsicSize.Min),
                    keyState = if (keycode is Keycode.Character) keyStates[keycode.char] else null
                )
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
    val color = when (keyState) {
        TileUiState.Present -> MaterialTheme.colorScheme.present
        TileUiState.Correct -> MaterialTheme.colorScheme.correct
        TileUiState.Absent -> MaterialTheme.colorScheme.absent
        null -> MaterialTheme.colorScheme.surface
    }
    val contentColor = contentColorFor(backgroundColor = color)
    Surface(
        modifier = modifier.padding(horizontal = 2.dp, vertical = 4.dp),
        onClick = onPressed,
        color = color,
        contentColor = contentColor,
        shape = RoundedCornerShape(4.dp),
        shadowElevation = 4.dp,
        role = Role.Button
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (keycode) {
                Keycode.Backspace -> {
                    Icons.Backspace(modifier = Modifier.align(Alignment.Center))
                }
                is Keycode.Character -> {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = keycode.char,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
                Keycode.Enter -> {
                    Icons.Enter(modifier = Modifier.align(Alignment.Center))
                }
            }

        }
    }
}

@Preview
@Composable
internal fun KeyboardPreview() {
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
        )
    }
}

@Preview
@Composable
internal fun KeyboardPreviewDark() {
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
            onKeyPressed = {}
        )
    }
}