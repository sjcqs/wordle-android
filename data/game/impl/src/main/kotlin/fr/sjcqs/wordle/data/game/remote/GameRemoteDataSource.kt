package fr.sjcqs.wordle.data.game.remote

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class GameRemoteDataSource @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
) {

    init {
        firebaseDatabase.getReference(REF_DAILY_WORD)
            .keepSynced(true)
    }

    private suspend fun dailyWord(): DailyWord {
        val snapshot = firebaseDatabase.getReference(REF_DAILY_WORD).get()
            .await()
        return snapshot.getValue<DailyWord>() ?: error("Missing daily word")
    }

    fun watchDailyWord(): Flow<DailyWord> = callbackFlow {
        val dailyWordReference = firebaseDatabase.getReference(REF_DAILY_WORD)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue<DailyWord>()?.let {
                    trySendBlocking(it)
                }
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
data class DailyWord(
    var word: String = "",
    var expired_at: String = ""
)