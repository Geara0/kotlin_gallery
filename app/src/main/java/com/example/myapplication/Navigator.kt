package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.models.MainViewModel
import com.example.myapplication.screens.ImageScreen
import com.example.myapplication.screens.MainScreen

@Composable
fun Navigator() {
    val navController = rememberNavController()
    val mainViewModel = MainViewModel()
    NavHost(
        navController = navController,
        startDestination = "main",
    ) {
        composable("main") {
            MainScreen(
                navController = navController,
                viewModel = mainViewModel
            )
        }
        composable(
            "image/{index}"
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 0
            ImageScreen(
                navController = navController,
                viewModel = mainViewModel,
                imageIndex = index
            )
        }
    }
}


