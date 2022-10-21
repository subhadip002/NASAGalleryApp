package com.example.nasagalleryapp.domain

import com.example.nasagalleryapp.data.ImageLocalDataSource
import com.example.nasagalleryapp.data.ImageRepository
import com.example.nasagalleryapp.util.DataSource
import com.example.nasagalleryapp.util.DataSource.getSortedList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

@ExperimentalCoroutinesApi
class GetSortedImagesWithDateUseCaseTest {
    @Test
    fun getSortedImageList_checkListIsSorted() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val imageLocalDataSource = ImageLocalDataSource(dispatcher, DataSource.getJsonData())
        val imageRepository = ImageRepository(imageLocalDataSource, TestScope(dispatcher))
        val getSortedImagesWithDateUseCase = GetSortedImagesWithDateUseCase(imageRepository)

        val images = getSortedImagesWithDateUseCase()
        assertThat(images, `is`(getSortedList()))
    }
}