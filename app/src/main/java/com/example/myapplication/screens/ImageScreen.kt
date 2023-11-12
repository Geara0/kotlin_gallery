package com.example.myapplication.screens

import android.graphics.Bitmap
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import com.example.myapplication.models.MainViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun ImageScreen(navController: NavController, viewModel: MainViewModel, imageIndex: Int) {
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
                    viewModel.bitmapList?.let { btm ->
                        ImageViewContent(btm[imageIndex])
                    }
                }
            }
        )
    }
}

@Composable
private fun ImageViewContent(imageRes: Bitmap) {
    Image(
        bitmap = imageRes.asImageBitmap(),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Fit
    )
}
