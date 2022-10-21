package com.example.nasagalleryapp.util

import com.example.nasagalleryapp.data.ImageApiModel
import com.example.nasagalleryapp.ui.ImageItemUiState
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

object DataSource {
    private const val ASSET_BASE_PATH = "../app/src/main/assets/data.json"
    fun getJsonData(): String {
        val bufferedReader = BufferedReader(InputStreamReader(FileInputStream(ASSET_BASE_PATH)))
        val stringBuilder = StringBuilder()
        var line: String? = bufferedReader.readLine()
        while (line != null) {
            stringBuilder.append(line)
            line = bufferedReader.readLine()
        }
        return stringBuilder.toString()
    }

    fun getImageList(): List<ImageApiModel> {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter: JsonAdapter<Array<ImageApiModel>> =
            moshi.adapter(Array<ImageApiModel>::class.java)
        return jsonAdapter.fromJson(getJsonData())?.asList()!!
    }

    fun getSortedList(): List<ImageApiModel> {
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

    fun getInvalidJSON(): String = "[{\n" +
            "        \"copyright\": 0,\n" +
            "        \"date\": \"20-12-01\",\n" +
            "        \"explanation\": 0.0,\n" +
            "        \"hdurl\": 0.0,\n" +
            "        \"media_type\": 0.0,\n" +
            "        \"service_version\": 0_00000,\n" +
            "        \"title\": 1234,\n" +
            "        \"url\": BigInteger()\n" +
            "    }]"

    fun getImageItemUiStateByIndex(index: Int): ImageItemUiState {
        return getImageItemUiStateList()[index]
    }
}