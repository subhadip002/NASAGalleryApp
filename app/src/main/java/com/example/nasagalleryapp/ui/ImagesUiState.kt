package com.example.nasagalleryapp.ui

data class ImagesUiState(
    val loading: Boolean = true,
    val userMessage: String? = null,
    val imageItems: List<ImageItemUiState> = emptyList()
)

fun ImagesUiState.getImageByIndex(index: Int): ImageItemUiState? {
    return if (imageItems.size >= index)
        imageItems[index]
    else null
}
