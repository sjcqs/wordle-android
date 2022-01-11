package fr.sjcqs.wordle.data.game

import fr.sjcqs.wordle.annotations.DefaultDispatcher
import fr.sjcqs.wordle.data.game.entity.Game
import fr.sjcqs.wordle.data.game.entity.Guess
import fr.sjcqs.wordle.data.game.entity.TileState
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class GameRepositoryImpl @Inject constructor(
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher
) : GameRepository {
    private val gameFlow = MutableStateFlow(
        Game(
            word = "HORDE",
            guesses = listOf(
                Guess.Current,
                Guess.Empty,
                Guess.Empty,
                Guess.Empty,
                Guess.Empty,
                Guess.Empty,
            ),
        )
    )

    override val dailyGame: Flow<Game>
        get() = gameFlow

    override suspend fun submit(word: String) {
        withContext(defaultDispatcher) {
            val game = gameFlow.value
            if (game.isFinished) {
                return@withContext
            }

            val guess = computeGuess(expected = game.word, submitted = word)
            gameFlow.value = game.add(guess)
        }
    }

    private fun computeGuess(expected: String, submitted: String): Guess.Submitted {
        /*expected.map {  }*/
        return Guess.Submitted(
            word = submitted,
            tiles = listOf(
                TileState.Absent,
                TileState.Present,
                TileState.Correct,
                TileState.Absent,
                TileState.Absent,
            )
        )
    }

    companion object {
        private const val MAX_GUESSES = 6
    }
}