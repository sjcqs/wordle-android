package fr.sjcqs.wordle.data.game

import android.content.Context
import fr.sjcqs.wordle.annotations.ApplicationContext
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
    private val defaultDispatcher: CoroutineDispatcher,
    @ApplicationContext
    private val context: Context,
    // db datasource
    // network datasource
) : GameRepository {
    private val words: HashSet<String> = loadWordList()

    private fun loadWordList(): HashSet<String> {
        return context.assets.open("words.txt").bufferedReader()
            .readLines()
            .toHashSet()
    }

    private val gameFlow = MutableStateFlow(initialGame())

    private fun initialGame() = Game(
        word = words.random(),
        guesses = emptyList(),
        guessesCount = MAX_GUESSES,
    )

    override val dailyGame: Flow<Game> = gameFlow

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

    private fun computeGuess(expected: String, submitted: String): Guess {
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
        return Guess(
            word = submitted,
            tiles = submitted.indices.map { tiles.getOrDefault(it, TileState.Absent) })
    }


    companion object {
        private const val MAX_GUESSES = 6
    }
}