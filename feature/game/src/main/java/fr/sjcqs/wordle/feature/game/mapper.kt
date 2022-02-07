package fr.sjcqs.wordle.feature.game

import fr.sjcqs.wordle.data.game.entity.Game
import fr.sjcqs.wordle.data.game.entity.Guess
import fr.sjcqs.wordle.data.game.entity.TileState
import fr.sjcqs.wordle.data.settings.entity.KeyboardLayout
import fr.sjcqs.wordle.feature.game.component.KeyboardLayoutUiModel
import fr.sjcqs.wordle.feature.game.model.GameUiState
import fr.sjcqs.wordle.feature.game.model.GuessUiModel
import fr.sjcqs.wordle.ui.components.TileUiState
import java.time.Duration
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.MutableStateFlow

internal fun TileState.toUiModel(): TileUiState = when (this) {
    TileState.Correct -> TileUiState.Correct
    TileState.Absent -> TileUiState.Absent
    TileState.Present -> TileUiState.Present
}

private fun KeyboardLayout.toUiModel() = when (this) {
    KeyboardLayout.Qwerty -> KeyboardLayoutUiModel.Qwerty
    KeyboardLayout.Azerty -> KeyboardLayoutUiModel.Azerty
}

internal fun Game.toUiModel(
    keyboardLayout: KeyboardLayout,
    onRetry: () -> Unit,
    onSubmit: (String) -> Unit,
    onTyping: () -> Unit,
    onShare: (text: String) -> Unit,
    expiredInFlow: MutableStateFlow<Duration>
): GameUiState.Guessing {
    val guessUiModels = buildList {
        addAll(guesses.map(Guess::toUiModel))
        if (!isFinished) {
            add(GuessUiModel(isEditable = true))
            repeat(maxGuesses - size) {
                add(GuessUiModel())
            }
        }
    }
    val keyStates = letterStates
        .mapKeys { it.key.toString() }
        .mapValues { (_, tileState) -> tileState.toUiModel() }

    return GameUiState.Guessing(
        guesses = guessUiModels,
        keyboardLayout = keyboardLayout.toUiModel(),
        keyStates = keyStates,
        onTyping = onTyping,
        onSubmit = onSubmit,
        word = word,
        isFinished = isFinished,
        isWon = isWon,
        onRetry = onRetry,
        canRetry = true,
        expiredInFlow = expiredInFlow,
        share = onShare,
        sharedText = sharedText,
    )
}


internal fun Guess.toUiModel(): GuessUiModel = GuessUiModel(
    word = word,
    tileState = tiles.mapIndexed { index, state ->
        index to state.toUiModel()
    }.toMap()
)

val Game.sharedText: String
    get() {
        return buildString {
            val date = expiredAt.minusDays(1)
                .format(DateTimeFormatter.ofPattern("dd MMM"))
            println("${expiredAt.minusDays(1)}")
            val guesses = guesses
            val performance = "${if (isWon) guesses.size else "\uD83D\uDC80"}/${maxGuesses}"
            appendLine("Quel Mot ? ")
            appendLine("le $date - $performance")
            appendLine()
            guesses.forEach { guess ->
                guess.tiles.forEach { tile ->
                    when (tile) {
                        TileState.Correct -> append("\uD83D\uDFE9")
                        TileState.Absent -> append("⬛")
                        TileState.Present -> append("\uD83D\uDFE8")
                    }
                }
                appendLine()
            }
            appendLine()
            appendLine("wordlefr.page.link/app (Android)")
        }
    }

internal fun Duration.format(): String {
    return String.format(
        "%d:%02d:%02d",
        toHours(),
        (seconds % (60 * 60)) / 60,
        seconds % 60
    );
}
