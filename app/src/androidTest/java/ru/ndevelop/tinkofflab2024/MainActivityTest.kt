package ru.ndevelop.tinkofflab2024

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    @Before
    fun before() {
        Intents.init()
    }

    @After
    fun after() {
        Intents.release()
    }

    @Test
    fun testBottomNavigationView() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_popular)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.fragment_container)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


    @Test
    fun testRecyclerView() {
        Espresso.onView(ViewMatchers.withId(R.id.rv_movie_list))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.click()))

        Intents.intended(IntentMatchers.hasComponent("ru.ndevelop.tinkofflab2024.ui.aboutMovie.AboutMovieActivity"))
    }
}