package com.example.myapplication.composable

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp


@Composable
fun ImageGrid(
    images: List<Bitmap>?,
    onImageClick: (Int) -> Unit
) {
    if (images == null) return

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(all = 8.dp),
    ) {
        items(images.indices.toList()) { index ->
            Image(
                bitmap = images[index].asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(100.dp)
                    .padding(8.dp)
                    .clickable {
                        onImageClick(index)
                    },
                contentScale = ContentScale.Crop
            )
        }
    }
}

