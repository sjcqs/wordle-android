package fr.sjcqs.wordle.data.settings.local.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import fr.sjcqs.wordle.data.settings.entity.Settings
import fr.sjcqs.wordle.data.settings.local.LocalSettingsDataSource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty


class PreferencesLocalSettingsDataSource @Inject constructor(
    private val preferences: DataStore<Preferences>
) : LocalSettingsDataSource {
    private val settingsFlow = preferences.data
        .map { preferences ->
            preferences.toSettings()
        }.onEmpty { emit(initialSettings) }

    override suspend fun getSettings() = settingsFlow.first()

    override suspend fun updateSettings(settings: Settings) {
        preferences.edit { preferences ->
            preferences[Keys.keyboardLayout] = settings.keyboardLayout.name
            preferences[Keys.theme] = settings.theme.name
        }
    }


    override fun watchSettings(): Flow<Settings> = settingsFlow


}

