package fr.sjcqs.wordle.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.sjcqs.wordle.feature.game.screen.Game

private sealed interface Screen {
    val route: String

    object Guessing : Screen {
        override val route = "guessing"
    }

    companion object {
        val startDestination = Guessing.route
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Screen.startDestination) {
        guessingScreens(navController)
    }
}

private fun NavGraphBuilder.guessingScreens(navController: NavHostController) {
    gameScreen(navController)
}

private fun NavGraphBuilder.gameScreen(navController: NavHostController) {
    composable(route = Screen.Guessing.route) {
        Game()
    }
}

private fun NavBackStackEntry.requireArguments() = arguments ?: error("no arguments")
