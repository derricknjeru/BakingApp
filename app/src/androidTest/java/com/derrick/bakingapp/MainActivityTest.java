package com.derrick.bakingapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.derrick.bakingapp.UI.main.MainActivity;
import com.derrick.bakingapp.utils.EspressoIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    // Register your Idling Resource before any tests regarding this component
    @Before
    public void registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
    }

    @Test
    public void clickListItem_OpenDetailsActivity() {

        Context appContext = InstrumentationRegistry.getTargetContext();
        // Uses {@link Espresso#onData(org.hamcrest.Matcher)} to get a reference to a specific

        onView(allOf(withId(R.id.main_list), isDisplayed())).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(appContext.getString(R.string.default_widget_recipe))), click()));

        // availability of master_list_fragment id means we have open ddetails activity
        onView(withId(R.id.master_list_fragment)).check(matches(isDisplayed()));

    }


    // Unregister your Idling Resource so it can be garbage collected and does not leak any memory
    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource());
    }

}
