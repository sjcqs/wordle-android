package fr.sjcqs.wordle.lifecycle_logging

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object LifecycleLoggingModule {

    @Provides
    fun provideActivityLifecycleLogger(factory: ActivityLifecycleLoggerFactory) = factory.create()
}