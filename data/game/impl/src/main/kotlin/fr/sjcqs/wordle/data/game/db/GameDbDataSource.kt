package fr.sjcqs.wordle.data.game.db

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import fr.sjcqs.wordle.annotations.DefaultDispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GameDbDataSource @Inject constructor(
    private val gameQueries: GameQueries,
    @DefaultDispatcher
    private val dispatcherDispatcher: CoroutineDispatcher,
) {
    suspend fun getLatest(): Game? = withContext(dispatcherDispatcher) {
        gameQueries.lastGame()
            .executeAsOneOrNull()
    }

    fun watchAll() = gameQueries.getAll()
        .asFlow()
        .mapToList(dispatcherDispatcher)

    fun watchLatest(): Flow<Game?> = gameQueries.lastGame()
        .asFlow()
        .mapToOneOrNull(dispatcherDispatcher)

    suspend fun insertOrUpdate(game: Game) = withContext(dispatcherDispatcher) {
        gameQueries.insertOrReplace(
            word = game.word,
            expiredAt = game.expiredAt,
            guesses = game.guesses,
        )
    }
}