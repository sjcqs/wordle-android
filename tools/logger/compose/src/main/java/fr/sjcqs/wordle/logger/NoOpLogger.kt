package fr.sjcqs.wordle.logger

internal class NoOpLogger : Logger {
    override val defaultTag: String
        get() = "no-op"

    override fun log(level: Logger.Level, tag: String, throwable: Throwable, message: String?) {
        /* no-op */
    }

    override fun log(level: Logger.Level, throwable: Throwable, message: String?) {
        /* no-op */
    }

    override fun log(level: Logger.Level, tag: String, message: String) {
        /* no-op */
    }

    override fun log(level: Logger.Level, message: String) {
        /* no-op */
    }

    override fun v(tag: String, throwable: Throwable, message: String?) {
        /* no-op */
    }

    override fun v(throwable: Throwable, message: String?) {
        /* no-op */
    }

    override fun v(tag: String, message: String) {
        /* no-op */
    }

    override fun v(message: String) {
        /* no-op */
    }

    override fun i(tag: String, throwable: Throwable, message: String?) {
        /* no-op */
    }

    override fun i(throwable: Throwable, message: String?) {
        /* no-op */
    }

    override fun i(tag: String, message: String) {
        /* no-op */
    }

    override fun i(message: String) {
        /* no-op */
    }

    override fun w(tag: String, throwable: Throwable, message: String?) {
        /* no-op */
    }

    override fun w(throwable: Throwable, message: String?) {
        /* no-op */
    }

    override fun w(tag: String, message: String) {
        /* no-op */
    }

    override fun w(message: String) {
        /* no-op */
    }

    override fun e(tag: String, throwable: Throwable, message: String?) {
        /* no-op */
    }

    override fun e(throwable: Throwable, message: String?) {
        /* no-op */
    }

    override fun e(tag: String, message: String) {
        /* no-op */
    }

    override fun e(message: String) {
        /* no-op */
    }

    override fun d(tag: String, throwable: Throwable, message: String?) {
        /* no-op */
    }

    override fun d(throwable: Throwable, message: String?) {
        /* no-op */
    }

    override fun d(tag: String, message: String) {
        /* no-op */
    }

    override fun d(message: String) {
        /* no-op */
    }

}