package com.reactivemobile.ukpoliceapp.ui.welcome;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.reactivemobile.ukpoliceapp.MainActivity;
import com.reactivemobile.ukpoliceapp.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by donalocallaghan on 02/06/2017.
 */
@RunWith(AndroidJUnit4.class)
public class WelcomeFragmentTest {


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testToggleDisclaimer() {
        onView(withId(R.id.checkbox_disclaimer)).perform(click());
        onView(withId(R.id.proceed_button)).check(matches(isDisplayed()));
    }


    @org.junit.After
    public void tearDown() throws Exception {
    }

}