package fr.sjcqs.wordle.data.game.db

import com.squareup.sqldelight.ColumnAdapter
import java.time.LocalDate
import java.time.LocalDateTime

class LocalDateTimeAdapter : ColumnAdapter<LocalDateTime, String> {
    override fun decode(databaseValue: String): LocalDateTime = kotlin.runCatching {
        LocalDateTime.parse(databaseValue)
    }.recoverCatching {
        LocalDate.parse(databaseValue).atTime(23, 59, 59)
    }.getOrDefault(LocalDateTime.MIN)

    override fun encode(value: LocalDateTime): String = value.toString()
}