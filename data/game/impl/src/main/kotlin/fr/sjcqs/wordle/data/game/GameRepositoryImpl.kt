package fr.sjcqs.wordle.data.game

import android.content.Context
import fr.sjcqs.wordle.annotations.ApplicationContext
import fr.sjcqs.wordle.annotations.DefaultDispatcher
import fr.sjcqs.wordle.data.game.db.GameDbDataSource
import fr.sjcqs.wordle.data.game.db.fromDb
import fr.sjcqs.wordle.data.game.db.toDb
import fr.sjcqs.wordle.data.game.entity.Game
import fr.sjcqs.wordle.data.game.entity.Guess
import fr.sjcqs.wordle.data.game.entity.TileState
import fr.sjcqs.wordle.logger.Logger
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import fr.sjcqs.wordle.data.game.db.Game as DbGame

class GameRepositoryImpl @Inject constructor(
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    @ApplicationContext
    private val context: Context,
    private val dbDataSource: GameDbDataSource,
    private val logger: Logger,
    // network datasource
) : GameRepository {
    private lateinit var game: Game
    private val words: HashSet<String> = assertsWords("words.txt")

    override val maxGuesses: Int = MAX_GUESSES

    private fun assertsWords(fileName: String): HashSet<String> {
        return context.assets.open(fileName)
            .bufferedReader()
            .readLines()
            .toHashSet()
    }

    private fun randomGame() = Game(
        word = assertsWords("suggested_words.txt").random(),
        guesses = emptyList(),
        guessesCount = MAX_GUESSES,
        expiredAt = LocalDate.now().plusDays(1)
    )

    override val dailyGame: Flow<Game> = dbDataSource.watchLatest()
        .onStart {
            val latest = dbDataSource.getLatest()
            if (latest == null || latest.isExpired) {
                val randomGame = randomGame()
                dbDataSource.insertOrUpdate(randomGame.toDb())
            }
        }.map { it.fromDb(MAX_GUESSES) }
        .onEach { game = it }
        .distinctUntilChanged()

    override suspend fun submit(word: String): Boolean {
        return withContext(defaultDispatcher) {
            if (words.contains(word)) {
                if (game.isFinished) {
                    return@withContext false
                }

                val guess = computeGuess(expected = game.word, submitted = word)
                val updatedGame = game.add(guess)
                dbDataSource.insertOrUpdate(updatedGame.toDb())
                true
            } else {
                false
            }
        }
    }

    override suspend fun refresh() {
        withContext(defaultDispatcher) {
            val randomGame = randomGame()
            dbDataSource.insertOrUpdate(randomGame.toDb())
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

    private val DbGame.isExpired: Boolean
        get() = expiredAt < LocalDate.now()

    companion object {
        private const val MAX_GUESSES = 6
    }
}