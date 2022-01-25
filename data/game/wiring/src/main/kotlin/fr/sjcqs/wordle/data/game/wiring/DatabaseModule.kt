package fr.sjcqs.wordle.data.game.wiring

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.sjcqs.wordle.annotations.ApplicationContext
import fr.sjcqs.wordle.data.game.Database
import fr.sjcqs.wordle.data.game.db.Game
import fr.sjcqs.wordle.data.game.db.GameQueries
import fr.sjcqs.wordle.data.game.db.GuessesAdapter
import fr.sjcqs.wordle.data.game.db.LocalDateTimeAdapter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    fun driver(@ApplicationContext context: Context): SqlDriver = AndroidSqliteDriver(
        Database.Schema,
        context,
        DATABASE_FILE_NAME,
    )

    @Provides
    @Singleton
    fun database(driver: SqlDriver) = Database(
        driver = driver,
        gameAdapter = Game.Adapter(
            LocalDateTimeAdapter(),
            GuessesAdapter()
        )
    )

    @Provides
    fun gameQueries(database: Database): GameQueries = database.gameQueries

    private const val DATABASE_FILE_NAME = "database.db"
}