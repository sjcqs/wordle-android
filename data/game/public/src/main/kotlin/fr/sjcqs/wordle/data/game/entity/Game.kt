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
    val tileLetters: Map<Int, Map<Char, TileState>> = guesses
        .fold(mutableMapOf<Int, MutableMap<Char, TileState>>()) { tilesState, guess ->
            guess.word.onEachIndexed { index, letter ->
                val tileState = guess.tiles[index]
                if (tileState != TileState.Present) {
                    tilesState.compute(index) { _, letters ->
                        (letters ?: mutableMapOf()).apply {
                            put(letter, tileState)
                        }
                    }
                    if (tileState == TileState.Absent &&
                        guesses.none { guess -> guess.tiles.contains(TileState.Present) }
                    ) {
                        tilesState.replaceAll { _, letters ->
                            letters.apply {
                                compute(letter) { _, currentTileState ->
                                    when (currentTileState) {
                                        TileState.Present,
                                        TileState.Correct -> currentTileState
                                        TileState.Absent,
                                        null -> TileState.Absent
                                    }
                                }
                            }
                        }
                    }
                }
            }
            tilesState
        }

    fun add(guess: Guess) = copy(guesses = guesses.plus(guess))

}