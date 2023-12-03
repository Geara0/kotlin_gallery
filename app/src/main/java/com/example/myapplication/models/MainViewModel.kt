package com.example.myapplication.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var bitmapList: MutableList<Bitmap>? = null
    var fileNames: List<Uri>? = null
    var rootDirUri: Uri? = null
    fun setBitmapList(context: Context, uri: Uri) {
        if (Build.VERSION.SDK_INT < 28) {
            rootDirUri = uri
            val a = DocumentFile.fromTreeUri(context, uri)
            fileNames = a?.listFiles()?.map { file ->
                file.uri
            }

            bitmapList = a?.listFiles()?.map { e ->
                MediaStore.Images.Media.getBitmap(
                    context.contentResolver,
                    e.uri
                )
            }?.toMutableList()
        } else {
            val a = DocumentFile.fromTreeUri(context, uri)
            fileNames = a?.listFiles()?.map { file ->
                file.uri
            }

            bitmapList = a?.listFiles()?.map { e ->
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(
                        context.contentResolver, e.uri
                    )
                )
            }?.toMutableList()
        }
    }
}
