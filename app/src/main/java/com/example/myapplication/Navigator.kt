package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.screens.ImageScreen
import com.example.myapplication.screens.MainScreen

@Composable
fun Navigator() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main",
    ) {
        composable("main") { MainScreen(navController = navController); }
        composable(
            "image/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            ImageScreen(
                navController = navController,
                imageResId = backStackEntry.arguments!!.getInt("id")
            )
        }
    }
}

