package fr.sjcqs.wordle

import android.content.Context
import android.content.res.Resources
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.sjcqs.wordle.annotations.ApplicationContext
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext as HiltApplicationContext

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {
    private const val dataBaseUrl =
        "https://wordle-android-default-rtdb.europe-west1.firebasedatabase.app/"

    @Provides
    @ApplicationContext
    fun provideApplicationContext(@HiltApplicationContext context: Context) = context

    @Provides
    @ApplicationContext
    fun provideApplicationResources(
        @HiltApplicationContext context: Context
    ): Resources = context.resources

    @Provides
    @Singleton
    fun provideFirebaseApp(
        @HiltApplicationContext context: Context
    ): FirebaseApp = FirebaseApp.initializeApp(context) ?: error("Couldn't initialise app")

    @Provides @Singleton
    fun provideFirebaseDatabase(app: FirebaseApp) = Firebase.database(app, dataBaseUrl).apply {
        setPersistenceEnabled(true)
    }

}