package fr.sjcqs.wordle.data.game.db

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import fr.sjcqs.wordle.annotations.IoDispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GameDbDataSource @Inject constructor(
    private val gameQueries: GameQueries,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
) {

    suspend fun getLatest(): Game? = withContext(ioDispatcher) {
        gameQueries.lastGame()
            .executeAsOneOrNull()
    }

    suspend fun getAll(): List<Game> = withContext(ioDispatcher) {
        gameQueries.getAll().executeAsList()
    }

    fun watchAll() = gameQueries.getAll()
        .asFlow()
        .mapToList(ioDispatcher)

    fun watchLatest(): Flow<Game> = gameQueries.lastGame()
        .asFlow()
        .mapToOne(ioDispatcher)

    suspend fun insertOrUpdate(game: Game) = withContext(ioDispatcher) {
        gameQueries.insertOrReplace(
            word = game.word,
            expiredAt = game.expiredAt,
            guesses = game.guesses,
        )
    }
}