package fr.sjcqs.wordle.lifecycle_logging

import fr.sjcqs.wordle.logger.Logger
import javax.inject.Inject

class ActivityLifecycleLoggerFactory @Inject constructor(private val logger: Logger) {
    fun create() = ActivityLifecycleLogger(logger)
}