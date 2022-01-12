package fr.sjcqs.wordle.data.game.entity

data class Game(
    val word: String,
    val currentGuess: Int,
    val guessesCount: Int,
    val guesses: List<Guess>,
    val isFinished: Boolean = false
) {
    fun add(guess: Guess): Game {
        val isFinished = currentGuess == guessesCount - 1 || guess.word == word
        return copy(
            guesses = guesses.plus(guess),
            currentGuess = if (isFinished) -1 else currentGuess + 1,
            isFinished = isFinished
        )
    }
}