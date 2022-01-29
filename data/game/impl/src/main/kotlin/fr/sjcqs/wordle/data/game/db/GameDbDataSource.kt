package fr.sjcqs.wordle.data.game.db

import kotlinx.coroutines.flow.Flow

interface GameDbDataSource {
    suspend fun getLatest(): Game?
    fun watchAll(): Flow<List<Game>>
    fun watchLatest(): Flow<Game?>
    suspend fun insertOrUpdate(game: Game)
}