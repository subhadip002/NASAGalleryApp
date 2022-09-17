package com.example.nasagalleryapp.data

import android.content.Context
import com.example.nasagalleryapp.DataSource.getExpectedList
import com.example.nasagalleryapp.DataSource.getFirstData
import com.example.nasagalleryapp.DataSource.getJsonData
import com.example.nasagalleryapp.R
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test

class ImageRepositoryTest {

    private val mockContext = mockk<Context>()
    private val mockImageDataSource = spyk(ImageDataSource(mockContext), recordPrivateCalls = true)

    private val imageRepository = ImageRepository(mockImageDataSource)

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        every { mockContext.getString(R.string.image_date_format) } returns "yyyy-MM-dd"
        every { mockImageDataSource["getJsonData"]() } returns getJsonData()
    }

    @Test
    fun getImageList_isNullOrEmpty_false() {
        assertThat(imageRepository.getImageList().isNullOrEmpty(), `is`(false))
    }

    @Test
    fun getImageList_checkListIsSorted() {
        assertThat(
            imageRepository.getImageList(),
            `is`(getExpectedList())
        )
    }

    @Test
    fun getImageList_validateData() {
        assertThat(
            imageRepository.getImageList()?.get(0), `is`(getFirstData())
        )
    }

    @Test
    fun getImageList_validateRepository_withDataSource() {
        assertThat(
            imageRepository.getImageList(), `is`(mockImageDataSource.getImageList())
        )
    }
}