package com.example.nasagalleryapp.ui

import com.example.nasagalleryapp.DataSource.getExpectedList
import com.example.nasagalleryapp.DataSource.getFirstData
import com.example.nasagalleryapp.DataSource.getImageByIndex
import com.example.nasagalleryapp.DataSource.getImageUiStateFromImage
import com.example.nasagalleryapp.data.ImageRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test

class ImageViewModelTest {
    private val mockImageRepository = spyk(ImageRepository(mockk())) {
        every { getImages() } returns getExpectedList()
    }
    private val viewModel = ImageViewModel(mockImageRepository)

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun getImageListUiItems_isEmpty_false() {
        assertThat(viewModel.imageListUiItems.isEmpty(), `is`(false))
    }

    @Test
    fun getImageListUiItems_validateImageOrderAndData() {
        for (i in 0 until getExpectedList().size) {
            val image = getImageByIndex(i)
            val expected = getImageUiStateFromImage(image)
            assertThat(viewModel.getImage(i), `is`(expected))
        }
    }

    @Test
    fun getImage_validateData() {
        val actual = viewModel.imageListUiItems[0]
        val expected = getFirstData()
        assertThat(actual, `is`(getImageUiStateFromImage(expected)))
    }
}