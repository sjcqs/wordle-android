package fr.sjcqs.wordle.data.game.entity

data class Game(
    val word: String,
    val guessesCount: Int,
    val guesses: List<Guess>,
    val isFinished: Boolean = false
) {
    val isWon: Boolean = word == guesses.lastOrNull()?.word
    val tileLetters: Map<Int, Map<Char, TileState>> = guesses
        .fold(mutableMapOf()) { tilesState, guess ->
            guess.word.onEachIndexed { index, letter ->
                val tileState = guess.tiles[index]
                if (tileState != TileState.Present) {
                    tilesState.compute(index) { _, letters ->
                        letters.orEmpty().plus(letter to tileState)
                    }
                }
            }
            tilesState
        }

    fun add(guess: Guess) = copy(
        guesses = guesses.plus(guess),
        isFinished = guesses.size + 1 == guessesCount || guess.word == word
    )
}

data class LetterState(
    val letter: Char,
    val state: TileState
)