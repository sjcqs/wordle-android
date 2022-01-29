package fr.sjcqs.wordle.data.game.db.impl

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import fr.sjcqs.wordle.annotations.DefaultDispatcher
import fr.sjcqs.wordle.data.game.db.Game
import fr.sjcqs.wordle.data.game.db.GameDbDataSource
import fr.sjcqs.wordle.data.game.db.GameQueries
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SqldelightGameDbDataSource @Inject constructor(
    private val gameQueries: GameQueries,
    @DefaultDispatcher
    private val dispatcherDispatcher: CoroutineDispatcher,
): GameDbDataSource {
    override suspend fun getLatest(): Game? = withContext(dispatcherDispatcher) {
        gameQueries.lastGame()
            .executeAsOneOrNull()
    }

    override fun watchAll(): Flow<List<Game>> = gameQueries.getAll()
        .asFlow()
        .mapToList(dispatcherDispatcher)

    override fun watchLatest(): Flow<Game?> = gameQueries.lastGame()
        .asFlow()
        .mapToOneOrNull(dispatcherDispatcher)

    override suspend fun insertOrUpdate(game: Game) = withContext(dispatcherDispatcher) {
        gameQueries.insertOrReplace(
            word = game.word,
            expiredAt = game.expiredAt,
            guesses = game.guesses,
        )
    }
}