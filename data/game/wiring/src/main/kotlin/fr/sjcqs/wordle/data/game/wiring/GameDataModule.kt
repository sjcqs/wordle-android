package fr.sjcqs.wordle.data.game.wiring

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.sjcqs.wordle.data.game.GameRepository
import fr.sjcqs.wordle.data.game.GameRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface GameDataModule {

    @Binds
    @Singleton
    fun repository(impl: GameRepositoryImpl): GameRepository
}