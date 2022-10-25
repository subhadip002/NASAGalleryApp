package com.example.nasagalleryapp.util

import com.example.nasagalleryapp.data.ImageApiModel
import com.example.nasagalleryapp.di.ImagesJson
import com.example.nasagalleryapp.ui.ImageItemUiState
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DataSource @Inject constructor(@ImagesJson private val imageJson: String) {

    private fun getImageList(): List<ImageApiModel> {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter: JsonAdapter<Array<ImageApiModel>> =
            moshi.adapter(Array<ImageApiModel>::class.java)
        return jsonAdapter.fromJson(imageJson)?.asList()!!
    }

    private fun getSortedList(): List<ImageApiModel> {
        val list = getImageList()
        val sortedList = list.sortedByDescending {
            it.date?.let { date ->
                SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.ENGLISH
                ).parse(date)?.time
            }
        }
        return sortedList
    }

    fun getImageItemUiStateList(): List<ImageItemUiState> {
        val sortedList = getSortedList()
        val imageItems = sortedList.map {
            ImageItemUiState(
                explanation = it.explanation ?: "",
                hdurl = it.hdurl ?: "",
                title = it.title ?: "",
                url = it.url ?: "",
                isOffline = false
            )
        }
        return imageItems
    }

    fun getImageItemUiStateByIndex(index: Int): ImageItemUiState {
        return getImageItemUiStateList()[index]
    }
}