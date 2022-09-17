package com.example.nasagalleryapp

import com.example.nasagalleryapp.data.Image
import com.example.nasagalleryapp.data.ImageItemUiState
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

    fun getExpectedList(): List<Image> {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter: JsonAdapter<Array<Image>> =
            moshi.adapter(Array<Image>::class.java)
        val list = jsonAdapter.fromJson(getJsonData())?.asList()
        val sortedList =
            list?.sortedWith(compareBy {
                it.date?.let { date ->
                    SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.ENGLISH
                    ).parse(date)?.time
                }
            })
        return sortedList!!
    }

    fun getImageByIndex(index: Int): Image {
        return getExpectedList()[index]
    }

    fun getFirstData(): Image {
        return Image(
            copyright = "ESA/HubbleNASA",
            date = "2019-12-01",
            explanation = "Why does this galaxy have a ring of bright blue stars?  Beautiful island universe Messier 94 lies a mere 15 million light-years distant in the northern constellation of the Hunting Dogs (Canes Venatici). A popular target for Earth-based astronomers, the face-on spiral galaxy is about 30,000 light-years across, with spiral arms sweeping through the outskirts of its broad disk. But this Hubble Space Telescope field of view spans about 7,000 light-years across M94's central region. The featured close-up highlights the galaxy's compact, bright nucleus, prominent inner dust lanes, and the remarkable bluish ring of young massive stars. The ring stars are all likely less than 10 million years old, indicating that M94 is a starburst galaxy that is experiencing an epoch of rapid star formation from inspiraling gas. The circular ripple of blue stars is likely a wave propagating outward, having been triggered by the gravity and rotation of a oval matter distributions. Because M94 is relatively nearby, astronomers can better explore details of its starburst ring.    Astrophysicists: Browse 2,000+ codes in the Astrophysics Source Code Library",
            hdurl = "https://apod.nasa.gov/apod/image/1912/M94_Hubble_1002.jpg",
            media_type = "image",
            service_version = "v1",
            title = "Starburst Galaxy M94 from Hubble",
            url = "https://apod.nasa.gov/apod/image/1912/M94_Hubble_960.jpg"
        )
    }

    fun getImageUiStateFromImage(image: Image): ImageItemUiState {
        return ImageItemUiState(
            explanation = image.explanation!!,
            hdurl = image.hdurl!!,
            title = image.title!!,
            url = image.url!!
        )
    }
}