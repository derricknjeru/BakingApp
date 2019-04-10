package com.derrick.bakingapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.derrick.bakingapp.UI.details.DetailsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class DetailsActivityTest {

    @Rule
    public ActivityTestRule<DetailsActivity> detailsActivityActivityTestRule = new ActivityTestRule<>(DetailsActivity.class);


    @Test
    public void clickListItem_OpenStepsActivity() {
        Context appContext = InstrumentationRegistry.getTargetContext();

        boolean isTablet = appContext.getResources().getBoolean(R.bool.isTablet);

        if (!isTablet) {

            onView(withId(R.id.master_list_fragment)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

            // availability of master_list_fragment id means we have open ddetails activity
            onView(withId(R.id.step_container)).check(matches(isDisplayed()));
        }

    }
}
