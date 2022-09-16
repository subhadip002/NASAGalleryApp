package com.example.nasagalleryapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasagalleryapp.data.ImageItemUiState
import com.example.nasagalleryapp.data.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageGridViewModel @Inject constructor(imageRepository: ImageRepository) :
    ViewModel() {
    val imageListUiItems = imageRepository.getImageList()?.map {
        ImageItemUiState(
            explanation = it.explanation ?: "",
            hdurl = it.hdurl ?: "",
            title = it.title ?: "",
            url = it.url ?: ""
        )
    } ?: listOf()
    var imageDetailCurrentImageIndex = MutableLiveData(0)
}