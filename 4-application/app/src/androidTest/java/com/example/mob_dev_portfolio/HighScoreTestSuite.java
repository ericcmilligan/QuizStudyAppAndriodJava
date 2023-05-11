package com.example.mob_dev_portfolio;


import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.mob_dev_portfolio.Database.QuizDatabase;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * An espresso test suite for testing the high scores section of the app
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class HighScoreTestSuite {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @BeforeClass
    public static void setupDatabaseForTests() {
        //Clear database for tests
        QuizDatabase db = QuizDatabase.getInstance(getApplicationContext());
        db.clearAllTables();
    }

    //Test that a high score can be shared via email
    @Test
    public void testShareHighScore() {
        //Initialize shared preferences
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.mob_dev_portfolio",
                Context.MODE_PRIVATE);

        //Stop first time helper pop-ups from appearing
        sharedPreferences.edit().putBoolean("first-run", false).commit();
        sharedPreferences.edit().putBoolean("first-run-qm-help", false).commit();
        sharedPreferences.edit().putBoolean("first-run-aq-help", false).commit();
        sharedPreferences.edit().putBoolean("first-run-eq-help", false).commit();

        //Go the the high score screen
        ViewInteraction goToHighScoresButton = onView(
                allOf(withId(R.id.high_score_button), withText("High Scores"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                0)));
        goToHighScoresButton.perform(scrollTo(), click());

        //Share the high score for the tag "Default"
        ViewInteraction shareHighScoreButton = onView(
                allOf(withId(R.id.shareHighScoreButton), withText("Share"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                1),
                        isDisplayed()));
        shareHighScoreButton.perform(click());
    }

    //Test that a high score can be reset for a tag
    @Test
    public void testResetHighScore(){
        //Initialize shared preferences
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.mob_dev_portfolio",
                Context.MODE_PRIVATE);

        //Stop first time helper pop-ups from appearing
        sharedPreferences.edit().putBoolean("first-run", false).commit();
        sharedPreferences.edit().putBoolean("first-run-qm-help", false).commit();
        sharedPreferences.edit().putBoolean("first-run-aq-help", false).commit();
        sharedPreferences.edit().putBoolean("first-run-eq-help", false).commit();

        //Go the the high score screen
        ViewInteraction goToHighScoresButton = onView(
                allOf(withId(R.id.high_score_button), withText("High Scores"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                0)));
        goToHighScoresButton.perform(scrollTo(), click());

        //Reset the high score for the tag "Default"
        ViewInteraction resetHighScoreButton = onView(
                allOf(withId(R.id.resetHighScoreButton), withText("Reset"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                0),
                        isDisplayed()));
        resetHighScoreButton.perform(click());

        //Confirm reset of high score
        ViewInteraction confirmResetHighScore = onView(
                allOf(withClassName(is("androidx.appcompat.widget.AppCompatButton")), withText("Yes"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.buttonPanel),
                                        0),
                                3)));
        confirmResetHighScore.perform(scrollTo(), click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
