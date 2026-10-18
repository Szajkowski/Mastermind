package pl.pollub.android.mastermind

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavHostController

@Composable
fun NavigationGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.PROFILE
    ) {

        /* =========================================================
           1) PROFILE → GAME (parametr: liczba kolorów)
           ========================================================= */
        composable(route = Routes.PROFILE,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = 400,
                        delayMillis = 300   // ← OPÓŹNIENIE
                    )
                ) + slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(
                        durationMillis = 400,
                        delayMillis = 300
                    )
                )
            },
            exitTransition = {
                fadeOut(tween(300)) +
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(400, easing = EaseOut)
                        )
            }) {
            ProfileScreen(
                onStartGame = { colors, playerId ->
                    navController.navigate("${Routes.GAME}/$colors/$playerId")
                }
            )

        }

        /* =========================================================
           GAME (argument: colors, playerId)
           ========================================================= */
        composable(
            route = "${Routes.GAME}/{colors}/{playerId}",
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = 400,
                        delayMillis = 300   // ← OPÓŹNIENIE
                    )
                ) + slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(
                        durationMillis = 400,
                        delayMillis = 300
                    )
                )
            },
            exitTransition = {
                fadeOut(tween(300)) +
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(400, easing = EaseOut)
                        )
            },
            arguments = listOf(
                navArgument("colors") { type = NavType.IntType },
                navArgument("playerId") { type = NavType.LongType }
            )
        ) { backStackEntry ->

            val colors = backStackEntry.arguments!!.getInt("colors")
            val playerId = backStackEntry.arguments!!.getLong("playerId")

            GameScreen(
                colorsCount = colors,
                playerId = playerId,

                /* 2) GAME → RESULTS (punkty, kolory) */
                onGameFinished = { points ->
                    navController.navigate("${Routes.RESULTS}/$points/$colors/$playerId")
                },

                /* 5) GAME → PROFILE */
                onQuitGame = {
                    navController.navigate(Routes.PROFILE) {
                        popUpTo(Routes.PROFILE) { inclusive = true }
                    }
                }
            )
        }

        /* =========================================================
           RESULTS (argument: points, colors, playerId)
           ========================================================= */
        composable(
            route = "${Routes.RESULTS}/{points}/{colors}/{playerId}",
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = 400,
                        delayMillis = 300   // ← OPÓŹNIENIE
                    )
                ) + slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(
                        durationMillis = 400,
                        delayMillis = 300
                    )
                )
            },
            exitTransition = {
                fadeOut(tween(300)) +
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(400, easing = EaseOut)
                        )
            },
            arguments = listOf(
                navArgument("points") { type = NavType.IntType },
                navArgument("colors") { type = NavType.IntType },
                navArgument("playerId") { type = NavType.LongType }
            )
        ) { backStackEntry ->

            val points = backStackEntry.arguments!!.getInt("points")
            val colors = backStackEntry.arguments!!.getInt("colors")
            val playerId = backStackEntry.arguments!!.getLong("playerId")


            ResultsScreen(
                points = points,

                /* 3) RESULTS → GAME (punkty) */
                onPlayAgain = {
                    navController.navigate(
                        "${Routes.GAME}/$colors/${playerId}"
                    ) {
                        popUpTo(Routes.GAME) { inclusive = true }
                    }
                },

                /* 4) RESULTS → PROFILE */
                onLogout = {
                    navController.navigate(Routes.PROFILE) {
                        popUpTo(Routes.PROFILE) { inclusive = true }
                    }
                }
            )
        }
    }
}
