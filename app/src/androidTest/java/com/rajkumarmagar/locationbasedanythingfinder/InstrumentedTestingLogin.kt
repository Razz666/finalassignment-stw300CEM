package com.rajkumarmagar.locationbasedanythingfinder

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.rajkumarmagar.locationbasedanythingfinder.ui.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class InstrumentedTestingLogin {
    @get:Rule
    val testRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun checkLogin() {
        onView(withId(R.id.etUsername1))
                .perform(typeText("qwerty"))

        onView(withId(R.id.etPassword1))
                .perform(typeText("123"))

        closeSoftKeyboard()

        onView(withId(R.id.btnLogin1))
                .perform(click())

        Thread.sleep(3000)

        onView(withId(R.id.container))
                .check(matches(isDisplayed()))
    }
}