package com.example.nasagalleryapp.data

import com.example.nasagalleryapp.di.DefaultDispatcher
import com.example.nasagalleryapp.di.ImagesJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class ImageLocalDataSource @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @ImagesJson private val imagesJson: String
) {
    suspend fun getImages(): List<ImageApiModel> =
        withContext(defaultDispatcher) {
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            moshi.adapter(Array<ImageApiModel>::class.java).fromJson(imagesJson)?.asList()
                ?: emptyList()
        }
}