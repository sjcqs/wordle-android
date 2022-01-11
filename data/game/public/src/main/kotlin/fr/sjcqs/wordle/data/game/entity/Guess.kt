package fr.sjcqs.wordle.data.game.entity

sealed interface Guess {
    object Empty : Guess
    object Current : Guess {
        override fun equals(other: Any?) = false
    }

    data class Submitted(
        val word: String,
        val tiles: List<TileState>,
    ) : Guess
}

