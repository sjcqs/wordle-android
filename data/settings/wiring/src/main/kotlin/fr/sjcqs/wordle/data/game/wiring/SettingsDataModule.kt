package fr.sjcqs.wordle.data.game.wiring

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.sjcqs.wordle.data.settings.SettingsRepository
import fr.sjcqs.wordle.data.settings.SettingsRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface SettingsDataModule {

    @Binds
    @Singleton
    fun repository(impl: SettingsRepositoryImpl): SettingsRepository
}