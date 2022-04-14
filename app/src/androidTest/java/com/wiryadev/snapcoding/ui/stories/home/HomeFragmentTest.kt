package com.wiryadev.snapcoding.ui.stories.home

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.paging.ExperimentalPagingApi
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.wiryadev.snapcoding.JsonConverter
import com.wiryadev.snapcoding.R
import com.wiryadev.snapcoding.data.remote.network.SnapCodingApiConfig
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalPagingApi
@MediumTest
@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start(8888)
        SnapCodingApiConfig.BASE_URL = "http://127.0.0.1:8888/"
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getStoriesSuccess() {
        launchFragmentInContainer<HomeFragment>(null, R.style.Theme_SnapCoding)

        val mockResponse = MockResponse().apply {
            setResponseCode(200)
            setBody(JsonConverter.readStringFromFile("sample_response.json"))
        }
        mockWebServer.enqueue(mockResponse)

        // check title page
        onView(withText("Stories"))
            .check(matches(isDisplayed()))

        // check FAB
        onView(withId(R.id.fab_add_story))
            .check(matches(isDisplayed()))

        // check recyclerView
        onView(withId(R.id.rv_stories))
            .check(matches(isDisplayed()))

        // check first visible data
        onView(withText("Farel Farel"))
            .check(matches(isDisplayed()))
    }

}