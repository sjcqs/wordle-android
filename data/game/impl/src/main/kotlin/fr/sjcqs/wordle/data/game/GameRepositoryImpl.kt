package fr.sjcqs.wordle.data.game

import android.content.Context
import fr.sjcqs.wordle.annotations.ApplicationContext
import fr.sjcqs.wordle.annotations.DefaultDispatcher
import fr.sjcqs.wordle.data.game.entity.Game
import fr.sjcqs.wordle.data.game.entity.Guess
import fr.sjcqs.wordle.data.game.entity.TileState
import fr.sjcqs.wordle.logger.Logger
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class GameRepositoryImpl @Inject constructor(
    private val logger: Logger,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    @ApplicationContext
    private val context: Context
) : GameRepository {
    private val words: HashSet<String> = loadWordList()

    private fun loadWordList(): HashSet<String> {
        return context.assets.open("words.txt").bufferedReader()
            .readLines()
            .toHashSet()
    }

    private val gameFlow = MutableStateFlow(INITIAL_STATE)

    override val dailyGame: Flow<Game>
        get() = gameFlow

    override suspend fun submit(word: String): Boolean {
        return withContext(defaultDispatcher) {
            if (words.contains(word)) {
                val game = gameFlow.value
                if (game.isFinished) {
                    return@withContext false
                }

                val guess = computeGuess(expected = game.word, submitted = word)
                gameFlow.value = game.add(guess)
                true
            } else {
                false
            }
        }
    }

    private fun computeGuess(expected: String, submitted: String): Guess.Submitted {
        val letterVisits = mutableMapOf<Char, Int>()
        val tiles = buildMap {
            expected.onEachIndexed { index, expectedLetter ->
                if (expectedLetter == submitted[index]) {
                    letterVisits.compute(expectedLetter) { _, count ->
                        count?.plus(1) ?: 1
                    }
                    put(index, TileState.Correct)
                }
            }
            submitted.forEachIndexed { index, letter ->
                val expectedFrequency = expected.count { it == letter }

                val visits = letterVisits.getOrDefault(letter, 0)
                if (expected[index] != letter &&
                    expected.contains(letter) &&
                    visits < expectedFrequency
                ) {
                    letterVisits[letter] = visits + 1
                    put(index, TileState.Present)
                }
            }
        }
        return Guess.Submitted(
            word = submitted,
            tiles = submitted.indices.map { tiles.getOrDefault(it, TileState.Absent) })
    }

    companion object {
        private val INITIAL_STATE = Game(
            word = "GAREE",
            guesses = listOf(
                Guess.Current,
                Guess.Empty,
                Guess.Empty,
                Guess.Empty,
                Guess.Empty,
                Guess.Empty,
            ),
        )
    }

}