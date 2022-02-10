package fr.sjcqs.wordle.data.settings

import fr.sjcqs.wordle.annotations.DefaultDispatcher
import fr.sjcqs.wordle.data.settings.entity.GameMode
import fr.sjcqs.wordle.data.settings.entity.KeyboardLayout
import fr.sjcqs.wordle.data.settings.entity.Settings
import fr.sjcqs.wordle.data.settings.entity.Theme
import fr.sjcqs.wordle.data.settings.local.LocalSettingsDataSource
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class SettingsRepositoryImpl @Inject constructor(
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    private val localDataSource: LocalSettingsDataSource
) : SettingsRepository {
    override val settingsFlow: Flow<Settings> = localDataSource.watchSettings()
    override val settings: Settings
        get() = runBlocking { localDataSource.getSettings() }

    override suspend fun setKeyboardLayout(keyboardLayout: KeyboardLayout) {
        withContext(defaultDispatcher) {
            val newSettings = localDataSource.getSettings().copy(keyboardLayout = keyboardLayout)
            localDataSource.updateSettings(newSettings)
        }
    }

    override suspend fun setTheme(theme: Theme) {
        withContext(defaultDispatcher) {
            val newSettings = localDataSource.getSettings().copy(theme = theme)
            localDataSource.updateSettings(newSettings)
        }
    }

    override suspend fun setMode(mode: GameMode) {
        withContext(defaultDispatcher) {
            val newSettings = localDataSource.getSettings().copy(gameMode = mode)
            localDataSource.updateSettings(newSettings)
        }
    }
}