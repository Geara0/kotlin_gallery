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
    var bitmapList: List<Bitmap>? = null
        private set

    fun setBitmapList(context: Context, uri: Uri) {
        if (Build.VERSION.SDK_INT < 28) {
            val a = DocumentFile.fromTreeUri(context, uri)
            bitmapList = a?.listFiles()
                ?.map { e ->
                    MediaStore.Images.Media.getBitmap(
                        context.contentResolver,
                        e.uri
                    )
                }
        } else {
            val a = DocumentFile.fromTreeUri(context, uri)

            bitmapList = a?.listFiles()?.map { e ->
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(
                        context.contentResolver, e.uri
                    )
                )
            }
        }
    }
}
