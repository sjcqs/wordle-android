package fr.sjcqs.wordle.logger.wiring

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.sjcqs.wordle.logger.Logger
import fr.sjcqs.wordle.logger.TimberLogger
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface LoggerModule {

    @Binds
    @Singleton
    fun bindsLogger(impl: TimberLogger): Logger
}