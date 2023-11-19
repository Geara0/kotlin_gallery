package com.example.myapplication.screens

import android.app.Activity
import android.graphics.Bitmap
import android.view.View
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.models.MainViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.io.File
import java.io.FileOutputStream
import kotlin.math.abs
import kotlin.math.round

@Composable
fun ImageScreen(navController: NavController, viewModel: MainViewModel, imageIndex: Int) {
    var isAppBarVisible by remember {
        mutableStateOf(true)
    }
    val appBarHeight = animateDpAsState(if (isAppBarVisible) 56.dp else 0.dp, label = "")

    var isBottomAppBarVisible by remember {
        mutableStateOf(true)
    }
    val bottomAppBarHeight by animateDpAsState(
        targetValue = if (isBottomAppBarVisible) 56.dp else 0.dp,
        label = ""
    )
    val context = LocalContext.current
    MyApplicationTheme(darkTheme = isSystemInDarkTheme()) {
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    content = {},
                    modifier = Modifier.height(bottomAppBarHeight)
                )
            },
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
                    },
                    modifier = Modifier.height(appBarHeight.value)
                )
            },
            content = { padding ->
                Surface(
                    modifier = Modifier.padding(padding),
                    color = MaterialTheme.colorScheme.background
                ) {
                    viewModel.bitmapList?.let { btm ->
                        ImageViewContent(
                            i = imageIndex,
                            btm[imageIndex],
                            left = if (imageIndex > 0) btm[imageIndex - 1] else null,
                            right = if (imageIndex < btm.size - 1) btm[imageIndex + 1] else null,
                            navController = navController,
                            onImageTap = {
                                // Переключаем видимость Баров при тапе на изображение
                                isAppBarVisible = !isAppBarVisible
                                isBottomAppBarVisible = !isBottomAppBarVisible
                                toggleStatusBar(activity = context as Activity, isVisible = isAppBarVisible)
                            }
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun ImageViewContent(
    i: Int,
    imageRes: Bitmap,
    left: Bitmap?,
    right: Bitmap?,
    navController: NavController,
    onImageTap: () -> Unit
) {
    val context = LocalContext.current
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

    var move by remember {
        mutableIntStateOf(0)
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

            val rotated = round(abs(rotation).coerceIn(0f, 360f) / 90)

            var l = if (rotated % 2 == 0f) -maxX else -maxY
            var r = if (rotated % 2 == 0f) maxX else maxY
            if (left != null) {
                l = Float.MIN_VALUE
            }

            if (right != null) {
                r = Float.MAX_VALUE
            }

            when (rotated) {
                0f ->
                    offset = Offset(
                        x = (offset.x + panChange.x * scale).coerceIn(l, r),
                        y = (offset.y + panChange.y * scale).coerceIn(-maxY, maxY),
                    )

                1f ->
                    offset = Offset(
                        x = (offset.y + panChange.y * scale).coerceIn(l, r),
                        y = (offset.x + panChange.x * scale).coerceIn(-maxX, maxX),
                    )

                2f ->
                    offset = Offset(
                        x = -(offset.x + panChange.x * scale).coerceIn(l, r),
                        y = -(offset.y + panChange.y * scale).coerceIn(-maxY, maxY),
                    )

                3f ->
                    offset = Offset(
                        x = -(offset.y + panChange.y * scale).coerceIn(l, r),
                        y = -(offset.x + panChange.x * scale).coerceIn(-maxX, maxX),
                    )
            }

            move = when {
                offset.x > maxX + 100 -> 1
                -offset.x > maxX - 100 -> -1
                else -> 0
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
                                if (move == 1 && right != null) {
                                    val path = context.getExternalFilesDir(null)!!.absolutePath
                                    val tempFile = File(path, "tempFileName.jpg")
                                    val fOut = FileOutputStream(tempFile)
                                    right.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                                    fOut.close()

                                    navController.popBackStack()
                                    navController.navigate("backImage/${i - 1}")
                                } else if (move == -1 && left != null) {
                                    val path = context.getExternalFilesDir(null)!!.absolutePath
                                    val tempFile = File(path, "tempFileName.jpg")
                                    val fOut = FileOutputStream(tempFile)
                                    left.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                                    fOut.close()

                                    navController.popBackStack()
                                    navController.navigate("image/${i + 1}")
                                }
                                else {
                                    onImageTap()
                                }
                                rotation = round(abs(rotation) / 90) * 90
                            }
                        }
                    }
                },
            contentScale = ContentScale.Fit
        )
    }
}

fun toggleStatusBar(activity: Activity, isVisible: Boolean) {
    if (isVisible) {
        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    } else {
        activity.window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }
}
