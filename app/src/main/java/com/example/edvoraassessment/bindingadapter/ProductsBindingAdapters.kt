package com.example.edvoraassessment.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.example.edvoraassessment.R

class ProductsBindingAdapters {

    companion object {
        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
            imageView.load(imageUrl) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
        }
    }


}