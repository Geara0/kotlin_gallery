package com.example.myapplication.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun ImageScreen(navController: NavController, imageResId: Int) {
    MyApplicationTheme(darkTheme = isSystemInDarkTheme()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Image") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                "back",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                )
            },
            content = { padding ->
                Surface(
                    modifier = Modifier.padding(padding),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ImageViewContent(imageResId)
                }
            }
        )
    }
}

@Composable
private fun ImageViewContent(imageResId: Int) {
    Image(
        painter = painterResource(id = imageResId),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Fit
    )
}
