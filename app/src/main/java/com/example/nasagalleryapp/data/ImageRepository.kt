package com.example.nasagalleryapp.data

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepository @Inject constructor(@ApplicationContext val context: Context) {

    private var imageList: List<Image>? = null

    private fun setImageList() {
        val inputStream = context.assets.open("data.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        val json = String(buffer)
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter: JsonAdapter<Array<Image>> =
            moshi.adapter(Array<Image>::class.java)
        val list = jsonAdapter.fromJson(json)?.asList()
        val sortedList =
            list?.sortedWith(compareBy {
                it.date?.let { date ->
                    SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date)?.time
                }
            })
        imageList = sortedList
    }

    fun getImageList(): List<Image>? {
        if (imageList == null) {
            setImageList()
        }
        return imageList
    }
}