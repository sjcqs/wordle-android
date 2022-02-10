package fr.sjcqs.wordle.data.game

import fr.sjcqs.wordle.data.game.entity.Game
import fr.sjcqs.wordle.data.game.entity.Stats
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    val statsFlow: Flow<Stats>
    val maxGuesses: Int

    fun getCurrentGame(isInfinite: Boolean): Flow<Game>
    suspend fun submit(word: String): Boolean
    suspend fun refresh()
}