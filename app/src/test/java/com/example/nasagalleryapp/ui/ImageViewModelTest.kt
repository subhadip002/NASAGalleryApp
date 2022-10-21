package com.example.nasagalleryapp.ui

import com.example.nasagalleryapp.data.ImageLocalDataSource
import com.example.nasagalleryapp.data.ImageRepository
import com.example.nasagalleryapp.domain.GetSortedImagesWithDateUseCase
import com.example.nasagalleryapp.util.DataSource.getImageItemUiStateByIndex
import com.example.nasagalleryapp.util.DataSource.getImageItemUiStateList
import com.example.nasagalleryapp.util.DataSource.getInvalidJSON
import com.example.nasagalleryapp.util.DataSource.getJsonData
import com.example.nasagalleryapp.util.MainDispatcherRule
import com.example.nasagalleryapp.util.NetworkMonitor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNull.notNullValue
import org.hamcrest.core.IsNull.nullValue
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ImageViewModelTest {
    private val networkMonitor = object : NetworkMonitor {
        override val isOnline: Flow<Boolean>
            get() = flowOf(true)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun getImagesUiState_validateImagesUiStateDefaultValues() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val imageLocalDataSource = ImageLocalDataSource(dispatcher, getJsonData())
        val imageRepository = ImageRepository(imageLocalDataSource, TestScope(dispatcher))
        val getSortedImagesWithDateUseCase = GetSortedImagesWithDateUseCase(imageRepository)
        val viewModel = ImageViewModel(getSortedImagesWithDateUseCase, networkMonitor)

        val imagesUiState = viewModel.imagesUiState.value
        assertThat(imagesUiState.imageItems, `is`(emptyList()))
        assertThat(imagesUiState.loading, `is`(true))
        assertThat(imagesUiState.errorMessage, `is`(nullValue()))
    }

    @Test
    fun isOffline_false() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val imageLocalDataSource = ImageLocalDataSource(dispatcher, getJsonData())
        val imageRepository = ImageRepository(imageLocalDataSource, TestScope(dispatcher))
        val getSortedImagesWithDateUseCase = GetSortedImagesWithDateUseCase(imageRepository)
        val viewModel = ImageViewModel(getSortedImagesWithDateUseCase, networkMonitor)

        assertThat(viewModel.isOffline.value, `is`(false))
    }

    @Test
    fun setImages_validateImagesUiStateValues() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val imageLocalDataSource = ImageLocalDataSource(dispatcher, getJsonData())
        val imageRepository = ImageRepository(imageLocalDataSource, TestScope(dispatcher))
        val getSortedImagesWithDateUseCase = GetSortedImagesWithDateUseCase(imageRepository)
        val viewModel = ImageViewModel(getSortedImagesWithDateUseCase, networkMonitor)

        viewModel.setImages()
        advanceUntilIdle()
        val imagesUiState = viewModel.imagesUiState.value
        assertThat(imagesUiState.imageItems, `is`(getImageItemUiStateList()))
        assertThat(imagesUiState.loading, `is`(false))
        assertThat(imagesUiState.errorMessage, `is`(nullValue()))
        for (i in 0 until getImageItemUiStateList().size) {
            assertThat(imagesUiState.getImageByIndex(i), `is`(getImageItemUiStateByIndex(i)))
        }
    }

    @Test
    fun setImages_invalidJson_userMessageShown_errorMessage_null() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val imageLocalDataSource = ImageLocalDataSource(dispatcher, getInvalidJSON())
        val imageRepository = ImageRepository(imageLocalDataSource, TestScope(dispatcher))
        val getSortedImagesWithDateUseCase = GetSortedImagesWithDateUseCase(imageRepository)
        val viewModel = ImageViewModel(getSortedImagesWithDateUseCase, networkMonitor)

        viewModel.setImages()
        advanceUntilIdle()
        val imagesUiState = viewModel.imagesUiState.value
        assertThat(imagesUiState.imageItems, `is`(emptyList()))
        assertThat(imagesUiState.loading, `is`(false))
        assertThat(imagesUiState.errorMessage, `is`(notNullValue()))
        viewModel.userMessageShown()
        advanceUntilIdle()
        assertThat(viewModel.imagesUiState.value.errorMessage, `is`(nullValue()))
    }
}