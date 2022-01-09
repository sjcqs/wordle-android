package fr.sjcqs.wordle.haptics

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface HapticControllerModule {
    @Binds
    fun bindHapticsController(impl: VibratorHapticsController): HapticsController
}