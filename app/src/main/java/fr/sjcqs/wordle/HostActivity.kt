package fr.sjcqs.wordle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import fr.sjcqs.wordle.haptics.HapticsController
import fr.sjcqs.wordle.haptics.LocalHapticController
import fr.sjcqs.wordle.logger.LocalLogger
import fr.sjcqs.wordle.logger.Logger
import fr.sjcqs.wordle.navigation.AppNavHost
import fr.sjcqs.wordle.ui.theme.WordleTheme
import javax.inject.Inject

@AndroidEntryPoint
internal class HostActivity : ComponentActivity() {
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var hapticsController: HapticsController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent { HostCompositionLocalProvider { AppContent(isSystemInDarkTheme()) } }
    }

    @Composable
    private fun HostCompositionLocalProvider(content: @Composable () -> Unit) {
        CompositionLocalProvider(
            LocalLogger provides logger,
            LocalHapticController provides hapticsController,
            content = content
        )
    }

    @Composable
    private fun AppContent(isSystemInDarkTheme: Boolean = isSystemInDarkTheme()) {
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = !isSystemInDarkTheme
            )
        }
        WordleTheme(isInDarkTheme = isSystemInDarkTheme) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {

                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    AppNavHost()
                }
            }
        }
    }
}