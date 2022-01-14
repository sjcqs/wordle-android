package fr.sjcqs.wordle.data.game.db

import com.squareup.sqldelight.ColumnAdapter
import java.time.LocalDate

class LocalDateAdapter : ColumnAdapter<LocalDate, String> {
    override fun decode(databaseValue: String): LocalDate = LocalDate.parse(databaseValue)

    override fun encode(value: LocalDate): String = value.toString()
}