package fr.sjcqs.wordle.data.game.entity

sealed interface Guess {
    object Empty : Guess
    object Editing : Guess
    data class Submitted(
        val word: String,
        val letters: Map<Char, LetterState>,
    ) : Guess
}

