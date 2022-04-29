package com.example.safemedicare;


import androidx.test.core.app.ActivityScenario;

import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


public class UI_testing {

    @Test
    public void testActivity() {

        ActivityScenario activityScenario = ActivityScenario.launch(Schedule_Activity.class);

        onView(withId(R.id.buttonAddEvent)).perform(click());
        //check if the second activity on view
        onView(withId(R.id.addEventPageID)).check(matches(isDisplayed()));

    }}