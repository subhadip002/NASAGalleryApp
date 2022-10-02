package com.example.nasagalleryapp.ui

data class ImagesUiState(
    val isLoading: Boolean = false,
    val userMessages: String? = null,
    val imageItems: List<ImageItemUiState> = listOf()
)

fun ImagesUiState.getImageByIndex(index: Int): ImageItemUiState? {
    return if (imageItems.size >= index)
        imageItems[index]
    else null
}
