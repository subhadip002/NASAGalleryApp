package com.example.nasagalleryapp.domain

import com.example.nasagalleryapp.data.ImageApiModel
import com.example.nasagalleryapp.data.ImageRepository
import kotlinx.coroutines.coroutineScope
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GetSortedImagesWithDateUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(): List<ImageApiModel> = coroutineScope {
        imageRepository.getImages().sortedByDescending {
            it.date?.let { date ->
                SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.ENGLISH
                ).parse(date)?.time
            }
        }
    }
}