package fr.sjcqs.wordle.data.game

import android.content.Context
import fr.sjcqs.wordle.annotations.ApplicationContext
import fr.sjcqs.wordle.annotations.DefaultDispatcher
import fr.sjcqs.wordle.data.game.db.GameDbDataSource
import fr.sjcqs.wordle.data.game.db.fromDb
import fr.sjcqs.wordle.data.game.db.toDb
import fr.sjcqs.wordle.data.game.entity.Game
import fr.sjcqs.wordle.data.game.entity.Guess
import fr.sjcqs.wordle.data.game.entity.Stats
import fr.sjcqs.wordle.data.game.entity.TileState
import fr.sjcqs.wordle.data.game.remote.DailyWord
import fr.sjcqs.wordle.data.game.remote.GameRemoteDataSource
import fr.sjcqs.wordle.logger.Logger
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameRepositoryImpl @Inject constructor(
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    @ApplicationContext
    private val context: Context,
    private val dbDataSource: GameDbDataSource,
    private val remoteDataSource: GameRemoteDataSource,
) : GameRepository {
    private val scope = CoroutineScope(defaultDispatcher + SupervisorJob())
    private lateinit var game: Game
    private val words: HashSet<String>
        get() {
            return context.assets.open("words.txt")
                .bufferedReader()
                .readLines()
                .toHashSet()
        }

    override val maxGuesses: Int = MAX_GUESSES

    init {
        scope.launch {
            remoteDataSource.watchDailyWord()
                .onEach { dailyWord ->
                    val latest = dbDataSource.getLatest()
                    if (latest?.word != dailyWord.word) {
                        val dailyGame = dailyWord.toGame()
                        dbDataSource.insertOrUpdate(dailyGame.toDb())
                    }
                }.retry()
                .launchIn(scope)
        }
    }

    override val dailyGameFlow: Flow<Game> = dbDataSource.watchLatest()
        .filterNotNull()
        .map { it.fromDb(MAX_GUESSES) }
        .map {
            if (it.isExpired) remoteDataSource.getDailyWord().toGame() else it
        }.onEach { game = it }
        .distinctUntilChanged()

    override val statsFlow: Flow<Stats> = dbDataSource.watchAll()
        .map { games -> games.map { it.fromDb(MAX_GUESSES) } }
        .combine(
            dbDataSource.watchLatest()
                .map { it?.fromDb(MAX_GUESSES) }) { allGames, latestGame ->
            val wonGames = allGames.filter { it.isWon }

            val currentStreak = allGames.takeWhile { it.isWon }.size

            var maxStreak = currentStreak
            var streak = 0
            allGames.forEach { game ->
                if (game.isWon) {
                    streak += 1
                } else {
                    if (streak > maxStreak) {
                        maxStreak = streak
                    }
                    streak = 0
                }
            }
            val distributions = wonGames
                .groupBy { it.guesses.size }
                .mapValues { (_, games) -> games.size }
            val played = allGames.count { it.isFinished }
            Stats(
                played = played,
                winRate = (wonGames.size.toDouble() / played).coerceIn(0.0, 1.0),
                currentStreak = currentStreak,
                maxStreak = maxStreak,
                distributions = (1..MAX_GUESSES).associateWith { distributions[it] ?: 0 },
                dailyFinishedGame = latestGame
                    ?.takeIf { it.isFinished && !it.isExpired }
            )
        }

    override suspend fun submit(word: String): Boolean {
        return withContext(defaultDispatcher) {
            if (words.contains(word)) {
                if (game.isFinished) {
                    return@withContext true
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

    private val Game.isExpired: Boolean
        get() = expiredAt < LocalDateTime.now()

    private val DailyWord.expiredAt: LocalDateTime
        get() = LocalDateTime.parse(expired_at, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

    private fun DailyWord.toGame(): Game {
        return Game(
            word = word,
            expiredAt = expiredAt,
            maxGuesses = MAX_GUESSES
        )
    }

    companion object {
        private const val MAX_GUESSES = 6
    }
}