package fr.sjcqs.wordle

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.sjcqs.wordle.annotations.ApplicationContext
import dagger.hilt.android.qualifiers.ApplicationContext as HiltApplicationContext

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {
    @Provides
    @ApplicationContext
    fun provideApplicationContext(@HiltApplicationContext context: Context) = context

    @Provides
    @ApplicationContext
    fun provideApplicationResources(
        @HiltApplicationContext context: Context
    ): Resources = context.resources

}