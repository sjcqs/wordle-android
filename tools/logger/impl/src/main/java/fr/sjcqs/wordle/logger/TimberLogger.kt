package fr.sjcqs.wordle.logger

import android.util.Log
import fr.sjcqs.wordle.annotations.IsDebug
import fr.sjcqs.wordle.logger.Logger.Level
import timber.log.Timber
import javax.inject.Inject

class TimberLogger @Inject constructor(
    @IsDebug isDebug: Boolean
) : Logger {

    init {
        if (isDebug) Timber.plant(Timber.DebugTree())
    }

    override val defaultTag: String
        get() = "Wordle"

    override fun log(level: Level, tag: String, throwable: Throwable, message: String?) {
        logImpl(level, tag, throwable, message)
    }

    override fun log(level: Level, throwable: Throwable, message: String?) {
        logImpl(level, null, throwable, message)
    }

    override fun log(level: Level, tag: String, message: String) {
        logImpl(level, tag, null, message)
    }

    override fun log(level: Level, message: String) {
        logImpl(level, null, null, message)
    }

    override fun v(tag: String, throwable: Throwable, message: String?) {
        log(Level.Verbose, tag, throwable, message)
    }

    override fun v(throwable: Throwable, message: String?) {
        log(Level.Verbose, throwable, message)
    }

    override fun v(tag: String, message: String) {
        log(Level.Verbose, tag, message)
    }

    override fun v(message: String) {
        log(Level.Verbose, message)
    }

    override fun i(tag: String, throwable: Throwable, message: String?) {
        log(Level.Info, tag, throwable, message)
    }

    override fun i(throwable: Throwable, message: String?) {
        log(Level.Info, throwable, message)
    }

    override fun i(tag: String, message: String) {
        log(Level.Info, tag, message)
    }

    override fun i(message: String) {
        log(Level.Info, message)
    }

    override fun w(tag: String, throwable: Throwable, message: String?) {
        log(Level.Warn, tag, throwable, message)
    }

    override fun w(throwable: Throwable, message: String?) {
        log(Level.Warn, throwable, message)
    }

    override fun w(tag: String, message: String) {
        log(Level.Warn, tag, message)
    }

    override fun w(message: String) {
        log(Level.Warn, message)
    }

    override fun e(tag: String, throwable: Throwable, message: String?) {
        log(Level.Error, tag, throwable, message)
    }

    override fun e(throwable: Throwable, message: String?) {
        log(Level.Error, throwable, message)
    }

    override fun e(tag: String, message: String) {
        log(Level.Error, tag, message)
    }

    override fun e(message: String) {
        log(Level.Error, message)
    }

    override fun d(tag: String, throwable: Throwable, message: String?) {
        log(Level.Debug, tag, throwable, message)
    }

    override fun d(throwable: Throwable, message: String?) {
        log(Level.Debug, throwable, message)
    }

    override fun d(tag: String, message: String) {
        log(Level.Debug, tag, message)
    }

    override fun d(message: String) {
        log(Level.Debug, message)
    }

    private fun logImpl(
        level: Level,
        tag: String?,
        throwable: Throwable?,
        message: String?
    ) {
        Timber.tag(tag ?: defaultTag).log(level.asPriority(), throwable, message)
    }

    private fun Level.asPriority(): Int = when (this) {
        Level.Verbose -> Log.VERBOSE
        Level.Info -> Log.INFO
        Level.Warn -> Log.WARN
        Level.Error -> Log.ERROR
        Level.Debug -> Log.DEBUG
    }
}