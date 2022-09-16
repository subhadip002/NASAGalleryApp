package com.example.nasagalleryapp.data

import androidx.recyclerview.widget.DiffUtil

data class Image(
    val copyright: String?,
    val date: String?,
    val explanation: String?,
    val hdurl: String?,
    val media_type: String?,
    val service_version: String?,
    val title: String?,
    val url: String?
)

object ImageDiffCallback : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image) =
        oldItem.url == newItem.url

    override fun areContentsTheSame(oldItem: Image, newItem: Image) = oldItem == newItem
}