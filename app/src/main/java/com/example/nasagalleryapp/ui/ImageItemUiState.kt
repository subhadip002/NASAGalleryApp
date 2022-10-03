package com.example.nasagalleryapp.ui

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.DiffUtil

data class ImageItemUiState(
    val explanation: String = "",
    val hdurl: String = "",
    val title: String = "",
    val url: String = "",
    var thumbnail: Drawable? = null
)

object ImageDiffCallback : DiffUtil.ItemCallback<ImageItemUiState>() {
    override fun areItemsTheSame(oldItem: ImageItemUiState, newItem: ImageItemUiState) =
        oldItem.url == newItem.url

    override fun areContentsTheSame(oldItem: ImageItemUiState, newItem: ImageItemUiState) =
        oldItem == newItem
}