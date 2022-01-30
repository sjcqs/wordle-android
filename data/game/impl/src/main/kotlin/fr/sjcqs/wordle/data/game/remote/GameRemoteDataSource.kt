package fr.sjcqs.wordle.data.game.remote

import java.time.LocalDateTime
import kotlinx.coroutines.flow.Flow


interface GameRemoteDataSource {
    suspend fun getDailyWord(): DailyWord
    fun watchDailyWord(): Flow<DailyWord>
}

data class DailyWord(
    var word: String = "",
    var expiredAt: LocalDateTime = LocalDateTime.MIN
)