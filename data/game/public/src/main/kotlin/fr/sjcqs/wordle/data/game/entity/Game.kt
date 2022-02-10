package fr.sjcqs.wordle.data.game.entity

import java.time.LocalDateTime

data class Game(
    val word: String,
    val maxGuesses: Int,
    val expiredAt: LocalDateTime,
    val isInfinite: Boolean,
    val guesses: List<Guess> = emptyList()
) {
    val isFinished: Boolean = guesses.lastOrNull()?.word == word || guesses.size >= maxGuesses
    val isWon: Boolean = word == guesses.lastOrNull()?.word
    val letterStates: Map<Char, TileState> = guesses
        .fold(mutableMapOf()) { letterStates, guess ->
            guess.word.onEachIndexed { index, letter ->
                val guessState = guess.tiles[index]
                letterStates.compute(letter) { _, currentState ->
                    if (guessState == TileState.Correct) {
                        TileState.Correct
                    } else {
                        when (currentState) {
                            TileState.Correct,
                            TileState.Present -> currentState
                            TileState.Absent,
                            null -> guessState
                        }
                    }
                }
            }
            letterStates
        }

    fun add(guess: Guess) = copy(guesses = guesses.plus(guess))

}