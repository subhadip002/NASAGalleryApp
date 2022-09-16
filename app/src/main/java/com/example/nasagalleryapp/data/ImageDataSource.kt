package com.example.nasagalleryapp.data

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val FILE_NAME = "data.json"

class ImageDataSource @Inject constructor(@ApplicationContext val context: Context) {

    fun getImageList(): List<Image>? {
        val inputStream = context.assets.open(FILE_NAME)
        val buffer = ByteArray(inputStream.available())
        inputStream.read(buffer)
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter: JsonAdapter<Array<Image>> =
            moshi.adapter(Array<Image>::class.java)
        val list = jsonAdapter.fromJson(String(buffer))?.asList()
        val sortedList =
            list?.sortedWith(compareBy {
                it.date?.let { date ->
                    SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date)?.time
                }
            })
        return sortedList
    }
}