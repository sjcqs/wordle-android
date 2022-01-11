package fr.sjcqs.wordle.data.game.entity

sealed interface Guess {
    object Empty : Guess
    object Current : Guess
    data class Submitted(
        val word: String,
        val tiles: List<TileState>,
    ) : Guess
}

