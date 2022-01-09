package fr.sjcqs.wordle.logger

interface Logger {
    val defaultTag: String

    fun log(level: Level, tag: String = defaultTag, throwable: Throwable, message: String? = null)
    fun log(level: Level, throwable: Throwable, message: String? = null)
    fun log(level: Level, tag: String = defaultTag, message: String)
    fun log(level: Level, message: String)

    fun v(tag: String = defaultTag, throwable: Throwable, message: String? = null)
    fun v(throwable: Throwable, message: String? = null)
    fun v(tag: String = defaultTag, message: String)
    fun v(message: String)

    fun i(tag: String = defaultTag, throwable: Throwable, message: String? = null)
    fun i(throwable: Throwable, message: String? = null)
    fun i(tag: String = defaultTag, message: String)
    fun i(message: String)

    fun w(tag: String = defaultTag, throwable: Throwable, message: String? = null)
    fun w(throwable: Throwable, message: String? = null)
    fun w(tag: String = defaultTag, message: String)
    fun w(message: String)

    fun e(tag: String = defaultTag, throwable: Throwable, message: String? = null)
    fun e(throwable: Throwable, message: String? = null)
    fun e(tag: String = defaultTag, message: String)
    fun e(message: String)

    fun d(tag: String = defaultTag, throwable: Throwable, message: String? = null)
    fun d(throwable: Throwable, message: String? = null)
    fun d(tag: String = defaultTag, message: String)
    fun d(message: String)

    enum class Level {
        Verbose, Info, Warn, Error, Debug
    }
}