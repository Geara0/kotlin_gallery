package com.example.myapplication.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.composable.ImageGrid
import com.example.myapplication.models.MainViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.io.File
import java.io.FileOutputStream

@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel) {
    val context = LocalContext.current
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
                            modifier = Modifier.weight(1f).padding(all = 10.dp),
                            content = {
                                Text(text = "Select")
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
                                viewModel.setBitmapList(context, it)
                            }

                            viewModel.bitmapList?.let { btm ->
                                ImageGrid(
                                    images = btm,
                                ) { index ->
                                    val tempFile = File(path, "tempFileName.jpg")
                                    val fOut = FileOutputStream(tempFile)
                                    btm[index].compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                                    fOut.close()

                                    navController.navigate("image/$index")
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

