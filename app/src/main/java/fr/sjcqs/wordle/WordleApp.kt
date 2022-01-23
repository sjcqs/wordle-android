package fr.sjcqs.wordle

import android.app.Application
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import dagger.hilt.android.HiltAndroidApp
import fr.sjcqs.wordle.data.game.GameRepository
import fr.sjcqs.wordle.lifecycle_logging.ActivityLifecycleLoggerFactory
import javax.inject.Inject

@HiltAndroidApp
class WordleApp : Application() {
    @Inject
    lateinit var lifecycleLoggerFactory: ActivityLifecycleLoggerFactory

    @Inject
    lateinit var gameRepository: GameRepository

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(lifecycleLoggerFactory.create())

        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            SafetyNetAppCheckProviderFactory.getInstance()
        )
    }
}