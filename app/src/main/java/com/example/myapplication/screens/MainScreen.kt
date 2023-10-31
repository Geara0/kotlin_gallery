package com.example.myapplication.screens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.documentfile.provider.DocumentFile
import androidx.navigation.NavController
import com.example.myapplication.composable.ImageGrid
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.io.File
import java.io.FileOutputStream

@Composable
fun MainScreen(navController: NavController) {
    val context = LocalContext.current
    val bitmap = remember {
        mutableStateOf<List<Bitmap>?>(null)
    }
    val result = remember { mutableStateOf<Uri?>(null) }
    val b = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
        result.value = it
    }

    val path = context.getExternalFilesDir(null)!!.absolutePath

    MyApplicationTheme(darkTheme = isSystemInDarkTheme()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Kotlin gallery") },
                )
            },
            bottomBar = {
                BottomAppBar(
                    content = {
                        Button(
                            onClick = {
                                b.launch(Uri.parse("image/*"))
                            },
                            modifier = Modifier.weight(1f),
                            content = {
                                Column {
                                    Text(text = "Select")
                                }
                            }
                        )
                    }
                )
            },
            content = { padding ->
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.padding(padding)) {
                        Column {
                            result.value?.let {
                                if (Build.VERSION.SDK_INT < 28) {
                                    val a = DocumentFile.fromTreeUri(context, result.value!!)
                                    bitmap.value = a?.listFiles()
                                        ?.map { e ->
                                            MediaStore.Images.Media.getBitmap(
                                                context.contentResolver,
                                                e.uri
                                            )
                                        }
                                } else {
                                    val a = DocumentFile.fromTreeUri(context, result.value!!)

                                    bitmap.value = a?.listFiles()?.map { e ->
                                        ImageDecoder.decodeBitmap(
                                            ImageDecoder.createSource(
                                                context.contentResolver, e.uri
                                            )
                                        )
                                    }
                                }

                                bitmap.value?.let { btm ->
                                    ImageGrid(
                                        images = btm,
                                        onImageClick = { image ->
                                            val tempFile = File(path, "tempFileName.jpg")
                                            val fOut = FileOutputStream(tempFile)
                                            image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                                            fOut.close()

                                            navController.navigate("image")
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            },
        )
    }
}

