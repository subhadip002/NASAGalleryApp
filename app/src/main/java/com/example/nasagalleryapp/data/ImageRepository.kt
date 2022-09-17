package com.example.nasagalleryapp.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepository @Inject constructor(private val imageDataSource: ImageDataSource) {
    @Volatile
    private var imageList: List<Image>? = null
    fun getImageList(): List<Image>? {
        return imageList ?: synchronized(this) {
            val data = imageDataSource.getImageList()
            imageList = data
            data
        }
    }
}