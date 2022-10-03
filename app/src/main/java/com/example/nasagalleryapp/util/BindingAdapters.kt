package com.example.nasagalleryapp.util

import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.nasagalleryapp.R
import com.example.nasagalleryapp.ui.ImageGridAdapter
import com.example.nasagalleryapp.ui.ImageItemUiState

@BindingAdapter("imageList")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<ImageItemUiState>) {
    val adapter = recyclerView.adapter as ImageGridAdapter
    adapter.submitList(data)
}

@BindingAdapter("isNetworkAvailable", "imageItemUiState")
fun loadHDImage(
    imageView: ImageView,
    isNetworkAvailable: Boolean,
    imageItemUiState: ImageItemUiState
) {
    if (isNetworkAvailable || imageView.drawable == null) {
        imageView.load(imageItemUiState.hdurl) {
            placeholder(
                imageItemUiState.thumbnail ?: AppCompatResources.getDrawable(
                    imageView.context, R.drawable.loading_animation
                )
            )
            error(R.drawable.ic_broken_image)
        }
    }
}