@file:OptIn(ExperimentalAnimationApi::class)

package fr.sjcqs.wordle.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import fr.sjcqs.wordle.feature.game.screen.Game
import fr.sjcqs.wordle.feature.settings.screen.Settings
import fr.sjcqs.wordle.feature.stats.screen.Stats

private sealed interface Screen {
    val route: String

    object Game : Screen {
        override val route = "game"
    }

    object Stats : Screen {
        override val route = "stats"
    }

    object Settings : Screen {
        override val route = "settings"
    }

    companion object {
        val startDestination = Game.route
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberAnimatedNavController()
) {
    AnimatedNavHost(navController = navController, startDestination = Screen.startDestination) {
        gameScreen(navController)
        statsScreen(navController)
        settingsScreen(navController)
    }
}

private fun NavGraphBuilder.gameScreen(navController: NavHostController) {
    composable(route = Screen.Game.route) {
        Game(
            showStats = { navController.navigate(Screen.Stats.route) },
            showSettings = { navController.navigate(Screen.Settings.route) }
        )
    }
}

private fun NavGraphBuilder.statsScreen(navController: NavHostController) {
    composable(
        route = Screen.Stats.route,
        enterTransition = { slideInVertically(initialOffsetY = { it }) },
        exitTransition = { slideOutVertically(targetOffsetY = { it }) },
        popEnterTransition = { fadeIn(initialAlpha = 1f) },
        popExitTransition = { fadeOut(targetAlpha = 1f) }
    ) {
        Stats(onClose = { navController.navigateUp() })
    }
}

private fun NavGraphBuilder.settingsScreen(navController: NavHostController) {
    composable(
        route = Screen.Settings.route,
        enterTransition = { slideInVertically(initialOffsetY = { it }) },
        exitTransition = { slideOutVertically(targetOffsetY = { it }) },
        popEnterTransition = { fadeIn(initialAlpha = 1f) },
        popExitTransition = { fadeOut(targetAlpha = 1f) }
    ) {
        Settings(onClose = { navController.navigateUp() })
    }
}

private fun NavBackStackEntry.requireArguments() = arguments ?: error("no arguments")
