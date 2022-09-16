package com.example.nasagalleryapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasagalleryapp.data.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageGridViewModel @Inject constructor(private val imageRepository: ImageRepository) :
    ViewModel() {
    fun getImageList() = imageRepository.getImageList()
    var imageDetailCurrentImageIndex = MutableLiveData(0)
}