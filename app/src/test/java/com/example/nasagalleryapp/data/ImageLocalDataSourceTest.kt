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


class ImageLocalDataSourceTest {

    private val mockContext = mockk<Context>()
    private val mockImageLocalDataSource = spyk(ImageLocalDataSource(mockContext), recordPrivateCalls = true)

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        every { mockContext.getString(R.string.image_date_format) } returns "yyyy-MM-dd"
        every { mockImageLocalDataSource["getJsonData"]() } returns getJsonData()
    }

    @Test
    fun getImageList_isNullOrEmpty_false() {
        assertThat(mockImageLocalDataSource.getImages().isNullOrEmpty(), `is`(false))
    }

    @Test
    fun getImageList_checkListIsSorted() {
        assertThat(mockImageLocalDataSource.getImages(), `is`(getExpectedList()))
    }

    @Test
    fun getImageList_validateData() {
        assertThat(
            mockImageLocalDataSource.getImages()?.get(0), `is`(getFirstData())
        )
    }
}