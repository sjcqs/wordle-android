package fr.sjcqs.wordle.data.game.entity

data class Game(
    val word: String,
    val guessesCount: Int,
    val guesses: List<Guess>,
    val isFinished: Boolean = false
) {
    fun add(guess: Guess) = copy(
        guesses = guesses.plus(guess),
        isFinished = guesses.size + 1 == guessesCount || guess.word == word
    )
}