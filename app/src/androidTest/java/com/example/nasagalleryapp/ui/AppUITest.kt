package com.example.nasagalleryapp.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.nasagalleryapp.MainActivity
import com.example.nasagalleryapp.R
import com.example.nasagalleryapp.ui.image_grid.ImageGridAdapter
import com.example.nasagalleryapp.util.DataSource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@LargeTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class AppUITest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var dataSource: DataSource

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun shimmer_DisplayedInUi() = runTest {
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.shimmerFrameLayout)).check(matches(isDisplayed()))
    }

    @Test
    fun imageGrid_DisplayedInUi() = runTest {
        launchImageGridFragment()
        onView(withId(R.id.imageGrid)).check(matches(isDisplayed()))
    }

    @Test
    fun validateImageCollection_againstImageGrid() = runTest {
        launchImageGridFragment()
        for (i in listOf(
            0,
            (dataSource.getImageItemUiStateList().size - 1) / 2,
            dataSource.getImageItemUiStateList().size - 1
        )) {
            clickOnImageGridPosition(i)
            for (j in i until dataSource.getImageItemUiStateList().size) {
                validateImageCollection(j)
                if (j < dataSource.getImageItemUiStateList().size - 1) {
                    onView(withId(R.id.pager)).perform(swipeLeft())
                }
            }
            pressBackButton()
            clickOnImageGridPosition(i)
            for (j in i.downTo(0)) {
                validateImageCollection(j)
                if (j > 0) {
                    onView(withId(R.id.pager)).perform(swipeRight())
                }
            }
            pressBackButton()
        }
    }

    private fun launchImageGridFragment() {
        ActivityScenario.launch(MainActivity::class.java)
        Thread.sleep(2000)
    }

    private fun pressBackButton() {
        pressBack()
        ActivityScenario.launch(MainActivity::class.java)
        Thread.sleep(2000)
    }

    private fun clickOnImageGridPosition(position: Int) {
        onView(withId(R.id.imageGrid)).perform(
            RecyclerViewActions.scrollToPosition<ImageGridAdapter.ImageViewHolder>(position)
        )
        onView(withId(R.id.imageGrid)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ImageGridAdapter.ImageViewHolder>(
                position, click()
            )
        )
    }

    private fun validateImageCollection(position: Int) {
        val data = dataSource.getImageItemUiStateByIndex(position)
        onView(withText(data.title)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withText(data.explanation)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }
}