package fr.sjcqs.wordle.data.game.db

import com.squareup.sqldelight.ColumnAdapter
import fr.sjcqs.wordle.data.game.entity.Guess
import fr.sjcqs.wordle.data.game.entity.TileState

private val TileState.databaseId: Int
    get() = when (this) {
        TileState.Correct -> 0
        TileState.Absent -> 1
        TileState.Present -> 2
    }

class GuessesAdapter : ColumnAdapter<List<Guess>, String> {
    override fun decode(databaseValue: String): List<Guess> {
        return if (databaseValue.isEmpty()) {
            listOf()
        } else {
            databaseValue.split(";").map {
                val (word, tiles) = it.split(":")
                Guess(
                    word = word,
                    tiles = tiles
                        .split(",")
                        .map(TileState::valueOf)
                )
            }
        }
    }

    override fun encode(value: List<Guess>): String {
        return value.joinToString(";") { guess ->
            "${guess.word}:${guess.tiles.joinToString(",") { it.name }}"
        }
    }
}