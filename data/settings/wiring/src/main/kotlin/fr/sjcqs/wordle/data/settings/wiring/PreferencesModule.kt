package fr.sjcqs.wordle.data.settings.wiring

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.sjcqs.wordle.annotations.ApplicationContext


@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    fun preferences(@ApplicationContext context: Context) = context.settingsPreferences
}