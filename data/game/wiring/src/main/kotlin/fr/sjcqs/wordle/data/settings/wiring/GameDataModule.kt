package fr.sjcqs.wordle.data.settings.wiring

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.sjcqs.wordle.data.game.GameRepository
import fr.sjcqs.wordle.data.game.GameRepositoryImpl
import fr.sjcqs.wordle.data.game.assets.GameAssetsDataSource
import fr.sjcqs.wordle.data.game.assets.impl.AndroidGameAssetsDataSource
import fr.sjcqs.wordle.data.game.db.GameDbDataSource
import fr.sjcqs.wordle.data.game.db.impl.SqldelightGameDbDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface GameDataModule {

    @Binds
    @Singleton
    fun repository(impl: GameRepositoryImpl): GameRepository

    @Binds
    @Singleton
    fun dbDataSource(impl: SqldelightGameDbDataSource): GameDbDataSource

    @Binds
    @Singleton
    fun assetsDataSource(impl: AndroidGameAssetsDataSource): GameAssetsDataSource
}