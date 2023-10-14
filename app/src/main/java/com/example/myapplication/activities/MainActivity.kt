package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.example.myapplication.utils.ImageAdapter


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val gridview = findViewById<View>(R.id.gvImageDisplay) as GridView
        gridview.adapter = ImageAdapter(this)
        gridview.onItemClickListener = gridviewOnItemClickListener
    }

    private val gridviewOnItemClickListener =
        // Sending image id to FullScreenActivity
        OnItemClickListener { _, _, position, _ ->
            val i = Intent(
                applicationContext, ImageViewActivity::class.java
            )

            // passing array index
            i.putExtra("id", position)
            startActivity(i)
        }
}
