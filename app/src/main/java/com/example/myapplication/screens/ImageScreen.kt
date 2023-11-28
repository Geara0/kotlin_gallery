@file:OptIn(ExperimentalMaterialApi::class)

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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
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
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import kotlin.math.abs
import kotlin.math.round


@Composable
fun ImageScreen(navController: NavController, viewModel: MainViewModel, imageIndex: Int) {
    var isSystemChromeVisible by remember {
        mutableStateOf(true)
    }
    val appBarHeight = animateDpAsState(if (isSystemChromeVisible) 56.dp else 0.dp, label = "")

    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    var filter by remember {
        mutableStateOf(ColorFilter.colorMatrix(ColorMatrix()))
    }

    MyApplicationTheme(darkTheme = isSystemInDarkTheme()) {
        BottomSheetScaffold(scaffoldState = scaffoldState,
            sheetBackgroundColor = MaterialTheme.colorScheme.surface,
            sheetPeekHeight = 0.dp,
            sheetGesturesEnabled = false,
            sheetContent = {
                Box(
                    Modifier.fillMaxWidth(), contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "Filters",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.headlineMedium,
                    )
                }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextButton(
                        onClick = {
                            filter =
                                ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            "Black & White",
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    TextButton(

                        onClick = {
                            val contrast = 2f // 0f..10f (1 should be default)
                            val brightness = -180f // -255f..255f (0 should be default)
                            val colorMatrix = floatArrayOf(
                                contrast, 0f, 0f, 0f, brightness,
                                0f, contrast, 0f, 0f, brightness,
                                0f, 0f, contrast, 0f, brightness,
                                0f, 0f, 0f, 1f, 0f
                            )

                            filter =
                                ColorFilter.colorMatrix(ColorMatrix(colorMatrix))
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            "High contrast",
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    TextButton(
                        onClick = {
                            val colorMatrix = floatArrayOf(
                                -1f, 0f, 0f, 0f, 255f,
                                0f, -1f, 0f, 0f, 255f,
                                0f, 0f, -1f, 0f, 255f,
                                0f, 0f, 0f, 1f, 0f
                            )

                            filter =
                                ColorFilter.colorMatrix(ColorMatrix(colorMatrix))
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            "Inversion",
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    TextButton(
                        onClick = {
                            filter =
                                ColorFilter.tint(Color.Blue, blendMode = BlendMode.Luminosity)
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            "Blue lumen",
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    TextButton(
                        onClick = {
                            filter =
                                ColorFilter.tint(Color.Blue, blendMode = BlendMode.ColorBurn)
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            "Blue burn",
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    Button(onClick = {
                        // TODO: apply filter to image
                        filter = ColorFilter.colorMatrix(ColorMatrix())
                        scope.launch { scaffoldState.bottomSheetState.collapse() }
                    }, modifier = Modifier.padding(all = 10.dp), content = {
                        Text(text = "Apply")
                    })

                }
            },
            topBar = {
                TopAppBar(title = { Text("Image") }, navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            "back",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }, actions = {
                    Button(onClick = {
                        if (scaffoldState.bottomSheetState.isExpanded) {
                            filter =
                                ColorFilter.colorMatrix(ColorMatrix())
                            scope.launch { scaffoldState.bottomSheetState.collapse() }
                        } else {
                            scope.launch { scaffoldState.bottomSheetState.expand() }
                        }
                    }, modifier = Modifier.padding(all = 10.dp), content = {
                        val text =
                            if (scaffoldState.bottomSheetState.isExpanded) {
                                "Close filters"
                            } else {
                                "Add filter"
                            }
                        Text(text = text)
                    })
                }, modifier = Modifier.height(appBarHeight.value)
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
                            btm = btm,
                            navController = navController,
                            onImageTap = {
                                // Переключаем видимость Баров при тапе на изображение
                                isSystemChromeVisible = !isSystemChromeVisible
                                toggleStatusBar(
                                    activity = context as Activity,
                                    isVisible = isSystemChromeVisible,
                                )
                            },
                            filter = filter,
                        )
                    }
                }
            })
    }
}

@Composable
private fun ImageViewContent(
    i: Int,
    btm: List<Bitmap>,
    navController: NavController,
    onImageTap: () -> Unit,
    filter: ColorFilter
) {
    val imageRes = btm[i]
    val left = if (i > 0) btm[i - 1] else null
    val right = if (i < btm.size - 1) btm[i + 1] else null

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
    var rotatedPrev by remember {
        mutableFloatStateOf(0f);
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
            rotatedPrev = rotated

            var l = if (rotated % 2 == 0f) -maxX else -maxY
            var r = if (rotated % 2 == 0f) maxX else maxY
            if (right != null) {
                l = -Float.MAX_VALUE
            }

            if (left != null) {
                r = Float.MAX_VALUE
            }

            when (rotated) {
                0f -> offset = Offset(
                    x = (offset.x + panChange.x * scale).coerceIn(l, r),
                    y = (offset.y + panChange.y * scale).coerceIn(-maxY, maxY),
                )

                1f -> offset = Offset(
                    x = (offset.y + panChange.y * scale).coerceIn(l, r),
                    y = (offset.x + panChange.x * scale).coerceIn(-maxX, maxX),
                )

                2f -> offset = Offset(
                    x = -(offset.x + panChange.x * scale).coerceIn(l, r),
                    y = -(offset.y + panChange.y * scale).coerceIn(-maxY, maxY),
                )

                3f -> offset = Offset(
                    x = -(offset.y + panChange.y * scale).coerceIn(l, r),
                    y = -(offset.x + panChange.x * scale).coerceIn(-maxX, maxX),
                )
            }

            println("${offset.x}")
            println("${-offset.x} > ${maxX + 100}: ${-offset.x > maxX + 100}")
            println("${-offset.x} < ${maxX - 100}: ${-offset.x < maxX - 100}")

            move = if (offset.x < 0 && -offset.x > maxX + 100) {
                1
            } else if (offset.x > 0 && -offset.x < maxX - 100) {
                -1
            } else {
                0
            }

            if (rotatedPrev != rotated) {
                move = 0
            }

            // Rotation
            rotation += rotationChange
        }

        Image(bitmap = imageRes.asImageBitmap(),
            contentDescription = null,
            colorFilter = filter,
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
                                if (move == -1 && left != null) {
                                    val path = context.getExternalFilesDir(null)!!.absolutePath
                                    val tempFile = File(path, "tempFileName.jpg")
                                    val fOut = FileOutputStream(tempFile)
                                    left.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                                    fOut.close()

                                    navController.popBackStack()
                                    navController.navigate("backImage/${i - 1}")
                                } else if (move == 1 && right != null) {
                                    val path = context.getExternalFilesDir(null)!!.absolutePath
                                    val tempFile = File(path, "tempFileName.jpg")
                                    val fOut = FileOutputStream(tempFile)
                                    right.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                                    fOut.close()

                                    navController.popBackStack()
                                    navController.navigate("image/${i + 1}")
                                } else if (move == 0) {
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
        activity.window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
}
