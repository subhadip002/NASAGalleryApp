package com.example.nasagalleryapp.ui

import androidx.recyclerview.widget.DiffUtil

data class ImageItemUiState(
    val explanation: String = "",
    val hdurl: String = "",
    val title: String = "",
    val url: String = "",
    val isOffline: Boolean = false
)

object ImageDiffCallback : DiffUtil.ItemCallback<ImageItemUiState>() {
    override fun areItemsTheSame(oldItem: ImageItemUiState, newItem: ImageItemUiState) =
        oldItem.url == newItem.url

    override fun areContentsTheSame(oldItem: ImageItemUiState, newItem: ImageItemUiState) =
        oldItem == newItem
}