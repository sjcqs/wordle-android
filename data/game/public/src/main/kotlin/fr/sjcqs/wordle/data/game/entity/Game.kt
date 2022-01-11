package fr.sjcqs.wordle.data.game.entity

data class Game(
    val word: String,
    val guesses: List<Guess>,
    val isFinished: Boolean = false
) {
    fun add(submittedGuess: Guess.Submitted): Game {
        val indexOfCurrent = guesses.indexOfFirst { it is Guess.Current }
        return copy(
            guesses = guesses.mapIndexed { index, guess ->
                when (index) {
                    indexOfCurrent -> submittedGuess
                    indexOfCurrent + 1 -> Guess.Current
                    else -> guess
                }
            },
            isFinished = indexOfCurrent == guesses.lastIndex || submittedGuess.word == word
        )
    }
}