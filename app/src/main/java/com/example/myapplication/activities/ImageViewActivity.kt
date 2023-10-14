package com.example.myapplication.activities

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.example.myapplication.R
import com.example.myapplication.utils.ImageAdapter


class ImageViewActivity : Activity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_view)

        // get intent data
        val intent = intent

        // Selected image id
        val position = intent.extras!!.getInt("id")
        val imageAdapter = ImageAdapter(this)
        val imageView = findViewById<View>(R.id.full_image_view) as ImageView
        imageView.setImageResource(imageAdapter.mThumbIds[position])
    }
}