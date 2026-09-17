package com.example.cs381finalproject.ui.theme

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cs381finalproject.viewmodel.GymViewModel

@Composable
fun GymAppNavigation(viewModel: GymViewModel) {
    val navController = rememberNavController()

    // NavHost requirement for the screencast, now with custom transitions
    NavHost(
        navController = navController,
        startDestination = "home",
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        }
    ) {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToAdd = { navController.navigate("add") },
                onNavigateToDetail = { id -> navController.navigate("detail/$id") }
            )
        }

        composable("add") {
            AddLogScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Dynamic Route requirement
        composable(
            route = "detail/{logId}",
            arguments = listOf(navArgument("logId") { type = NavType.IntType })
        ) { backStackEntry ->
            val logId = backStackEntry.arguments?.getInt("logId") ?: 0
            DetailScreen(
                viewModel = viewModel,
                logId = logId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}