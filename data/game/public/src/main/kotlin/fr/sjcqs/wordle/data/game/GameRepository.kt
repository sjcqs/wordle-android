package fr.sjcqs.wordle.data.game

import fr.sjcqs.wordle.data.game.entity.Game
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    val dailyGame: Flow<Game>
    val maxGuesses: Int

    suspend fun submit(word: String): Boolean
    suspend fun refresh()
}