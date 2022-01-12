package fr.sjcqs.wordle.data.game.entity

data class Guess(
    val word: String,
    val tiles: List<TileState>,
)

