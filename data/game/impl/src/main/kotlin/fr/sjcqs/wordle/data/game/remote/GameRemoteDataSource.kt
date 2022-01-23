package fr.sjcqs.wordle.data.game.remote

import androidx.annotation.Keep
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine

class GameRemoteDataSource @Inject constructor(
    firebaseDatabase: FirebaseDatabase,
) {

    private val dailyWordRef = firebaseDatabase.getReference(REF_DAILY_WORD)

    init {
        dailyWordRef.keepSynced(true)
    }

    private suspend fun dailyWord(): DailyWord = suspendCancellableCoroutine { continuation ->
        dailyWordRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue<DailyWord>()?.let(continuation::resume)
            }

            override fun onCancelled(error: DatabaseError) {
                continuation.cancel(error.toException())
            }

        })
    }

    fun watchDailyWord(): Flow<DailyWord> = callbackFlow {
        val dailyWordReference = dailyWordRef
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue<DailyWord>()?.let { trySendBlocking(it) }
            }

            override fun onCancelled(error: DatabaseError) {
                dailyWordReference.removeEventListener(this)
            }

        }
        dailyWordReference.addValueEventListener(listener)
        trySendBlocking(dailyWord())
        awaitClose {
            dailyWordReference.removeEventListener(listener)
        }
    }

    companion object {
        private const val REF_DAILY_WORD = "/daily_word"
    }
}

@IgnoreExtraProperties
@Keep
data class DailyWord(
    var word: String = "",
    var expired_at: String = ""
)