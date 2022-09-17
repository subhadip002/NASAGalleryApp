package com.example.nasagalleryapp.util

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.nasagalleryapp.R
import com.example.nasagalleryapp.data.ImageItemUiState
import com.example.nasagalleryapp.ui.ImageGridAdapter

@BindingAdapter("imageList")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<ImageItemUiState>) {
    val adapter = recyclerView.adapter as ImageGridAdapter
    adapter.submitList(data)
}

@BindingAdapter("url")
fun bindImage(imgView: ImageView, url: String) {
    val imgUri = url.toUri().buildUpon().scheme("https").build()
    imgView.load(imgUri) {
        placeholder(R.drawable.loading_animation)
        error(R.drawable.ic_broken_image)
    }
}

@BindingAdapter("position")
fun bindRecyclerView(recyclerView: RecyclerView, position: Int) {
    recyclerView.scrollToPosition(position)
}