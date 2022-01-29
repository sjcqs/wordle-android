package fr.sjcqs.wordle.data.settings.wiring

import android.content.Context
import androidx.core.app.ComponentActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import fr.sjcqs.wordle.data.settings.entity.Settings
import fr.sjcqs.wordle.data.settings.local.impl.toSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking


val Context.settingsPreferences: DataStore<Preferences> by preferencesDataStore("settings")

val Context.settings: Settings
    get() = runBlocking {
        settingsPreferences.data.map(Preferences::toSettings).first()
    }

val ComponentActivity.settingsFlow: Flow<Settings>
    get() = settingsPreferences.data.map(Preferences::toSettings)