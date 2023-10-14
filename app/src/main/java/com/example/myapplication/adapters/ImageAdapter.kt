package com.example.myapplication.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ImageView


class ImageAdapter(c: Context?) : BaseAdapter() {
    private var mContext: Context? = c

    override fun getCount(): Int {
        return mThumbIds.size
    }

    override fun getItem(position: Int): Int {
        return mThumbIds[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView: ImageView
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = ImageView(mContext)
            imageView.layoutParams = AbsListView.LayoutParams(85, 85)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.setPadding(8, 8, 8, 8)
        } else {
            imageView = convertView as ImageView
        }
        imageView.setImageResource(mThumbIds[position])
        return imageView
    }

    // references to our images
    var mThumbIds = arrayOf(
        androidx.appcompat.R.drawable.btn_checkbox_unchecked_mtrl, androidx.constraintlayout.widget.R.drawable.abc_btn_borderless_material,
        androidx.constraintlayout.widget.R.drawable.btn_checkbox_checked_mtrl,androidx.constraintlayout.widget.R.drawable.abc_btn_check_to_on_mtrl_000,
        androidx.appcompat.R.drawable.btn_checkbox_unchecked_mtrl, androidx.constraintlayout.widget.R.drawable.abc_btn_borderless_material,
        androidx.constraintlayout.widget.R.drawable.btn_checkbox_checked_mtrl,androidx.constraintlayout.widget.R.drawable.abc_btn_check_to_on_mtrl_000,
        androidx.appcompat.R.drawable.btn_checkbox_unchecked_mtrl, androidx.constraintlayout.widget.R.drawable.abc_btn_borderless_material,
        androidx.constraintlayout.widget.R.drawable.btn_checkbox_checked_mtrl,androidx.constraintlayout.widget.R.drawable.abc_btn_check_to_on_mtrl_000,
        androidx.appcompat.R.drawable.btn_checkbox_unchecked_mtrl, androidx.constraintlayout.widget.R.drawable.abc_btn_borderless_material,
        androidx.constraintlayout.widget.R.drawable.btn_checkbox_checked_mtrl,androidx.constraintlayout.widget.R.drawable.abc_btn_check_to_on_mtrl_000,
    )
}