package fr.sjcqs.wordle.lifecycle_logging

import android.app.Activity
import android.app.Application
import android.os.Bundle
import fr.sjcqs.wordle.logger.Logger
import lifecycleTag

class ActivityLifecycleLogger(private val logger: Logger) : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        logger.d(
            activity.lifecycleTag(),
            "onCreated(savedInstanceState = $savedInstanceState)"
        )
    }

    override fun onActivityStarted(activity: Activity) {
        logger.d(activity.lifecycleTag(), "onStarted()")
    }

    override fun onActivityResumed(activity: Activity) {
        logger.d(activity.lifecycleTag(), "onResumed()")
    }

    override fun onActivityPaused(activity: Activity) {
        logger.d(activity.lifecycleTag(), "onPaused()")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        logger.d(activity.lifecycleTag(), "onSaveInstanceState(outState = $outState)")
    }

    override fun onActivityStopped(activity: Activity) {
        logger.d(activity.lifecycleTag(), "onStopped()")
    }

    override fun onActivityDestroyed(activity: Activity) {
        logger.d(activity.lifecycleTag(), "onDestroyed()")
    }
}