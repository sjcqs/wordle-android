package fr.sjcqs.wordle

import android.content.Context
import android.content.pm.ApplicationInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.sjcqs.wordle.annotations.ApplicationContext
import fr.sjcqs.wordle.annotations.IsDebug

@InstallIn(SingletonComponent::class)
@Module
internal object DebugModule {
    @Provides
    @IsDebug
    fun provideIsDebug(
        @ApplicationContext context: Context
    ) = context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0

}