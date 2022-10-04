package com.example.nasagalleryapp.data

import android.content.Context
import com.example.nasagalleryapp.di.DefaultDispatcher
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

private const val FILE_NAME = "data.json"

class ImageLocalDataSource @Inject constructor(
    @ApplicationContext val context: Context,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {
    private val moshi: Moshi by lazy {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    private fun getJsonData(): String {
        val inputStream = context.assets.open(FILE_NAME)
        val buffer = ByteArray(inputStream.available())
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer)
    }

    suspend fun getImages(): List<ImageApiModel> =
        withContext(defaultDispatcher) {
            moshi.adapter(Array<ImageApiModel>::class.java).fromJson(getJsonData())?.asList()
                ?: emptyList()
        }
}