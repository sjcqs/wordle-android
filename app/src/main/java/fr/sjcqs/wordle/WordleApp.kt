package fr.sjcqs.wordle

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import fr.sjcqs.wordle.lifecycle_logging.ActivityLifecycleLoggerFactory
import javax.inject.Inject

@HiltAndroidApp
class WordleApp : Application() {
    @Inject
    lateinit var lifecycleLoggerFactory: ActivityLifecycleLoggerFactory

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(lifecycleLoggerFactory.create())
    }
}