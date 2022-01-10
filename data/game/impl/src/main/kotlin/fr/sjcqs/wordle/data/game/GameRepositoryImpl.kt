package fr.sjcqs.wordle.data.game

import fr.sjcqs.wordle.annotations.DefaultDispatcher
import fr.sjcqs.wordle.data.game.entity.Game
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class GameRepositoryImpl @Inject constructor(
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher
) : GameRepository {
    override val dailyGame: Flow<Game>
        get() = TODO("Not yet implemented")

    override suspend fun submit(word: String) {
        TODO("Not yet implemented")
    }
}