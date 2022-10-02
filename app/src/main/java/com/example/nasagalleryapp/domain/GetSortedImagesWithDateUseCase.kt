package com.example.nasagalleryapp.domain

import com.example.nasagalleryapp.data.ImageApiModel
import com.example.nasagalleryapp.data.ImageRepository
import com.example.nasagalleryapp.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GetSortedImagesWithDateUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): List<ImageApiModel> = withContext(defaultDispatcher) {
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