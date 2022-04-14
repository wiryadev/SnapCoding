package com.wiryadev.snapcoding.ui.stories

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.wiryadev.snapcoding.R
import com.wiryadev.snapcoding.ui.stories.upload.CameraActivity
import com.wiryadev.snapcoding.ui.stories.upload.UploadActivity
import com.wiryadev.snapcoding.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun loadHomeAndDetail() {
        onView(withId(R.id.rv_stories))
            .check(matches(isDisplayed()))
        onView(withId(R.id.rv_stories)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                20, click()
            )
        )
        onView(withId(R.id.iv_story))
            .check(matches(isDisplayed()))
        onView(withId(R.id.tv_name))
            .check(matches(isDisplayed()))
        onView(withId(R.id.tv_date))
            .check(matches(isDisplayed()))
        onView(withId(R.id.tv_desc))
            .check(matches(isDisplayed()))
    }

    @Test
    fun loadUploadAndCamera() {
        Intents.init()
        onView(withId(R.id.fab_add_story)).perform(click())

        // check Upload Activity
        Intents.intended(hasComponent(UploadActivity::class.java.name))
        onView(withId(R.id.btn_gallery))
            .check(matches(isDisplayed()))
        onView(withId(R.id.btn_camera))
            .check(matches(isDisplayed()))
            .perform(click())

        // check CameraActivity
        Intents.intended(hasComponent(CameraActivity::class.java.name))
        onView(withId(R.id.btn_capture)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_switch_camera)).check(matches(isDisplayed()))
    }

    @Test
    fun loadMap() {
        onView(withId(R.id.navigation_map))
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withId(R.id.google_map))
            .check(matches(isDisplayed()))
    }

    @Test
    fun loadSetting() {
        onView(withId(R.id.navigation_setting))
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withText("Change Language"))
            .check(matches(isDisplayed()))
        onView(withText("Log Out"))
            .check(matches(isDisplayed()))
    }

}