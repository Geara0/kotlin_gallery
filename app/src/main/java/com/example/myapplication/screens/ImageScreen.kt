package com.example.myapplication.screens

import android.graphics.Bitmap
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.models.MainViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlin.math.abs
import kotlin.math.round

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
    val density = LocalDensity.current
    var height by remember {
        mutableStateOf(0.dp)
    }

    var scale by remember {
        mutableFloatStateOf(1f)
    }

    var rotation by remember {
        mutableFloatStateOf(0f)
    }
    val rotationAnim: Float by animateFloatAsState(targetValue = rotation, label = "rotation")

    var offset by remember {
        mutableStateOf(Offset.Zero)
    }

    BoxWithConstraints {
        val state = rememberTransformableState { zoomChange, panChange, rotationChange ->
            // Scale
            scale = (scale * zoomChange).coerceIn(1f, 10f)

            // Offset
            val extraWidth = (scale - 1) * constraints.maxWidth
            val extraHeight = (scale - 1) * height.value
            val maxX = extraWidth / 2
            val maxY = extraHeight / 2
            when (round(abs(rotation).coerceIn(0f, 360f) / 90)) {
                0f ->
                    offset = Offset(
                        x = (offset.x + panChange.x * scale).coerceIn(-maxX, maxX),
                        y = (offset.y + panChange.y * scale).coerceIn(-maxY, maxY),
                    )

                1f ->
                    offset = Offset(
                        x = (offset.y + panChange.y * scale).coerceIn(-maxY, maxY),
                        y = (offset.x + panChange.x * scale).coerceIn(-maxX, maxX),
                    )

                2f ->
                    offset = Offset(
                        x = -(offset.x + panChange.x * scale).coerceIn(-maxX, maxX),
                        y = -(offset.y + panChange.y * scale).coerceIn(-maxY, maxY),
                    )

                3f ->
                    offset = Offset(
                        x = -(offset.y + panChange.y * scale).coerceIn(-maxY, maxY),
                        y = -(offset.x + panChange.x * scale).coerceIn(-maxX, maxX),
                    )
            }


            // Rotation
            rotation += rotationChange
        }

        Image(
            bitmap = imageRes.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                    rotationZ = rotationAnim
                }
                .transformable(state)
                .onGloballyPositioned {
                    height = with(density) { it.size.height.toDp() }
                }
                .pointerInput(PointerEventType.Release) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            if (event.type == PointerEventType.Release) {
                                rotation = round(abs(rotation) / 90) * 90
                            }
                        }
                    }
                },
            contentScale = ContentScale.Fit
        )
    }
}

