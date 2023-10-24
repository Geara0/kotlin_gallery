package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.myapplication.R
import com.example.myapplication.composable.ImageGrid
import com.example.myapplication.ui.theme.MyApplicationTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ImageGrid(
                        images = getImagesIds(),
                        onImageClick = { image ->
                            startImageViewActivity(image)
                        }
                    )
                }
            }
        }
    }


    private fun getImagesIds(): List<Int> {
        val imageArray = resources.obtainTypedArray(R.array.image_array)

        val imageList = (0 until imageArray.length()).map {
            imageArray.getResourceId(it, 0)
        }
        return imageList
    }

    private fun startImageViewActivity(imageResId: Int) {
        val intent = Intent(this, ImageViewActivity::class.java)
        intent.putExtra("id", imageResId)
        startActivity(intent)
    }
}


