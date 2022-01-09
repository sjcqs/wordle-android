package fr.sjcqs.wordle.haptics

import android.content.Context
import android.os.Vibrator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.sjcqs.wordle.annotations.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
class VibratorModule {
    @Provides
    fun provideVibrator(@ApplicationContext context: Context): Vibrator {
        return context.getSystemService(Vibrator::class.java)
    }
}