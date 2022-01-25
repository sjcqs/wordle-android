package fr.sjcqs.wordle.data.game.entity

import java.time.LocalDate

data class Game(
    val word: String,
    val maxGuesses: Int,
    val expiredAt: LocalDate,
    val guesses: List<Guess> = emptyList(),
) {
    val isFinished: Boolean = guesses.lastOrNull()?.word == word || guesses.size >= maxGuesses
    val isWon: Boolean = word == guesses.lastOrNull()?.word
    val letterStates: Map<Char, TileState> = guesses
        .fold(mutableMapOf()) { letterStates, guess ->
            guess.word.onEachIndexed { index, letter ->
                val guessState = guess.tiles[index]
                letterStates.compute(letter) { _, currentState ->
                    when (currentState) {
                        TileState.Correct -> {
                            if (guessState == TileState.Present) guessState else currentState
                        }
                        TileState.Absent -> guessState
                        TileState.Present -> TileState.Present
                        null -> guessState
                    }
                }
            }
            letterStates
        }

    fun add(guess: Guess) = copy(guesses = guesses.plus(guess))

}