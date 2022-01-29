package fr.sjcqs.wordle.data.settings.local

import fr.sjcqs.wordle.data.settings.entity.Settings
import kotlinx.coroutines.flow.Flow

interface LocalSettingsDataSource {
    suspend fun getSettings(): Settings
    suspend fun updateSettings(settings: Settings)

    fun watchSettings(): Flow<Settings>
}