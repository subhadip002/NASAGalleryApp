package com.example.nasagalleryapp.data

import com.example.nasagalleryapp.util.DataSource.getImageList
import com.example.nasagalleryapp.util.DataSource.getJsonData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

@ExperimentalCoroutinesApi
class ImageRepositoryTest {

    @Test
    fun getImageList_checkListIsSorted() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val imageLocalDataSource = ImageLocalDataSource(dispatcher, getJsonData())
        val imageRepository = ImageRepository(imageLocalDataSource, TestScope(dispatcher))
        val images = imageRepository.getImages()
        assertThat(images, `is`(getImageList()))
    }

    @Test
    fun getImageList_validateRepository_withDataSource() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val imageLocalDataSource = ImageLocalDataSource(dispatcher, getJsonData())
        val imageRepository = ImageRepository(imageLocalDataSource, TestScope(dispatcher))
        val dataSourceImages = imageLocalDataSource.getImages()
        val repositoryImages = imageRepository.getImages()
        assertThat(
            repositoryImages, `is`(dataSourceImages)
        )
    }
}