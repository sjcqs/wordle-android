package fr.sjcqs.wordle

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.sjcqs.wordle.annotations.IsDebug

@InstallIn(SingletonComponent::class)
@Module
internal object DebugModule {
    @Provides
    @IsDebug
    fun provideIsDebug() = BuildConfig.DEBUG

}