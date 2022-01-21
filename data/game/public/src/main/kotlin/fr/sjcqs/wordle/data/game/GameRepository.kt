package fr.sjcqs.wordle.data.game

import fr.sjcqs.wordle.data.game.entity.Game
import fr.sjcqs.wordle.data.game.entity.Stats
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    val dailyGameFlow: Flow<Game>
    val statsFlow: Flow<Stats>
    val maxGuesses: Int

    suspend fun submit(word: String): Boolean
    suspend fun refresh()
}