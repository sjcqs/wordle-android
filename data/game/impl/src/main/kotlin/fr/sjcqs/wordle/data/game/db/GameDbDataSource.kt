package fr.sjcqs.wordle.data.game.db

import kotlinx.coroutines.flow.Flow

interface GameDbDataSource {
    suspend fun getLatest(isInfinite: Boolean = false): Game?
    fun watchAll(isInfinite: Boolean = false): Flow<List<Game>>
    fun watchLatest(isInfinite: Boolean = false): Flow<Game?>
    suspend fun insertOrUpdate(game: Game)
}