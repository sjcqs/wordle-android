package fr.sjcqs.wordle.data.settings.wiring

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.sjcqs.wordle.data.settings.SettingsRepository
import fr.sjcqs.wordle.data.settings.SettingsRepositoryImpl
import fr.sjcqs.wordle.data.settings.local.LocalSettingsDataSource
import fr.sjcqs.wordle.data.settings.local.impl.PreferencesLocalSettingsDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface SettingsDataModule {

    @Binds
    @Singleton
    fun repository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    @Singleton
    fun localDataSource(impl: PreferencesLocalSettingsDataSource): LocalSettingsDataSource
}