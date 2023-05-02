package com.example.mob_dev_portfolio;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class QuestionManagerTestSuite {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testAddingAQuestionToATag() {
        //Initialize shared preferences
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.mob_dev_portfolio",
                Context.MODE_PRIVATE);

        //Stop first time helper pop-ups from appearing
        sharedPreferences.edit().putBoolean("first-run", false).commit();
        sharedPreferences.edit().putBoolean("first-run-qm-help", false).commit();
        sharedPreferences.edit().putBoolean("first-run-aq-help", false).commit();

        //Visit the question manager by clicking the button on the homepage
        ViewInteraction goToQuestionManagerButton = onView(
                allOf(withId(R.id.question_manager_button), withText("Question Manager"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                0)));
        goToQuestionManagerButton.perform(scrollTo(), click());

        //Click add question floating action button to go the add question form
        ViewInteraction addQuestionFloatingActionButton = onView(
                allOf(withId(R.id.addQuestionButton), withContentDescription("Add question"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                4),
                        isDisplayed()));
        addQuestionFloatingActionButton.perform(click());

        //Input "Example question" as the text for the question title field
        ViewInteraction questionTitleEditText = onView(
                allOf(withId(R.id.editTextQuestionTitle),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                1),
                        isDisplayed()));
        questionTitleEditText.perform(scrollTo(), replaceText("Example Question"), closeSoftKeyboard());

        //Input "Example 1" as the text for the answer 1 field
        ViewInteraction answer1EditText = onView(
                allOf(withId(R.id.editTextAnswer1),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                1)));
        answer1EditText.perform(scrollTo(), replaceText("Example 1"), closeSoftKeyboard());

        //Input "Example 2" as the text for the answer 2 field
        ViewInteraction answer2EditText = onView(
                allOf(withId(R.id.editTextAnswer2),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                1)));
        answer2EditText.perform(scrollTo(), replaceText("Example 2"), closeSoftKeyboard());

        //Input "Example 3" as the text for the answer 2 field
        ViewInteraction answer3EditText = onView(
                allOf(withId(R.id.editTextAnswer3),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                1)));
        answer3EditText.perform(scrollTo(), replaceText("Example 3"), closeSoftKeyboard());

        //Input "Example 4" as the text for the answer 2 field
        ViewInteraction answer4EditText = onView(
                allOf(withId(R.id.editTextAnswer4),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        6),
                                1)));
        answer4EditText.perform(scrollTo(), replaceText("Example 4"), closeSoftKeyboard());

        //Submit the new question into the database
        ViewInteraction submitNewQuestion = onView(
                allOf(withId(R.id.submitButton), withText("Submit Button"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        8),
                                0)));
        submitNewQuestion.perform(scrollTo(), click());
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
