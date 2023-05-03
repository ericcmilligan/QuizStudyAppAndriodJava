package com.example.mob_dev_portfolio;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onData;
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
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.room.Database;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.mob_dev_portfolio.Database.QuizDatabase;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * An espresso test suite for testing the question manager section of the app
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class QuestionManagerTestSuite {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    //Test if a question can be added to a tag
    @Test
    public void testAddingAQuestionToATag() {
        //Clear database for test
        QuizDatabase db = QuizDatabase.getInstance(getApplicationContext());
        db.clearAllTables();

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

        //Input "Example 3" as the text for the answer 3 field
        ViewInteraction answer3EditText = onView(
                allOf(withId(R.id.editTextAnswer3),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                1)));
        answer3EditText.perform(scrollTo(), replaceText("Example 3"), closeSoftKeyboard());

        //Input "Example 4" as the text for the answer 4 field
        ViewInteraction answer4EditText = onView(
                allOf(withId(R.id.editTextAnswer4),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        6),
                                1)));
        answer4EditText.perform(scrollTo(), replaceText("Example 4"), closeSoftKeyboard());

        //Can submit the new question into the database
        ViewInteraction submitNewQuestion = onView(
                allOf(withId(R.id.submitButton), withText("Submit Button"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        8),
                                0)));
        submitNewQuestion.perform(scrollTo(), click());
    }

    //Test a tag name can be edited
    @Test
    public void testEditingATagName() {
        //Clear database for test
        QuizDatabase db = QuizDatabase.getInstance(getApplicationContext());
        db.clearAllTables();

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

        //Open the tag spinner
        ViewInteraction openTagSpinner = onView(
                allOf(withId(R.id.tagChooserSpinner),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                1),
                        isDisplayed()));
        openTagSpinner.perform(click());

        //Change selected tag to default
        DataInteraction changeTagToDefault = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        changeTagToDefault.perform(click());

        //Click edit tag button to open edit text pop-up for editing the selected tag's name
        ViewInteraction editTagButton = onView(
                allOf(withId(R.id.qmEditTagButton), withContentDescription("Edit tag button"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                2),
                        isDisplayed()));
        editTagButton.perform(click());

        //Input "Test Tag" into the edit text pop-up
        ViewInteraction editTagName = onView(
                allOf(childAtPosition(
                                allOf(withId(androidx.appcompat.R.id.custom),
                                        childAtPosition(
                                                withId(androidx.appcompat.R.id.customPanel),
                                                0)),
                                0),
                        isDisplayed()));
        editTagName.perform(replaceText("Test Tag"), closeSoftKeyboard());

        //Click "Ok" to submit the edited tag's name
        ViewInteraction submitEditedTagName = onView(
                allOf(withClassName(is("androidx.appcompat.widget.AppCompatButton")), withText("Ok"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.buttonPanel),
                                        0),
                                3)));
        submitEditedTagName.perform(scrollTo(), click());
    }

    //Test a tag can be deleted
    @Test
    public void testDeletingATag(){
        //Clear database for test
        QuizDatabase db = QuizDatabase.getInstance(getApplicationContext());
        db.clearAllTables();

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

        //Open the tag spinner
        ViewInteraction openTagSpinner = onView(
                allOf(withId(R.id.tagChooserSpinner),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                1),
                        isDisplayed()));
        openTagSpinner.perform(click());

        //Change selected tag to default
        DataInteraction changeTagToDefault = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        changeTagToDefault.perform(click());

        //Open delete tag pop-up
        ViewInteraction deleteTagButton = onView(
                allOf(withId(R.id.qmDeleteTagButton), withContentDescription("Delete tag button"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                3),
                        isDisplayed()));
        deleteTagButton.perform(click());

        //Delete the default tag from the database
        ViewInteraction submitDeleteTag = onView(
                allOf(withClassName(is("androidx.appcompat.widget.AppCompatButton")), withText("Yes"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.buttonPanel),
                                        0),
                                3)));
        submitDeleteTag.perform(scrollTo(), click());
    }

    //Test a question can be edited
    @Test
    public void testEditingAQuestion(){
        //Clear database for test
        QuizDatabase db = QuizDatabase.getInstance(getApplicationContext());
        db.clearAllTables();

        //Initialize shared preferences
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.mob_dev_portfolio",
                Context.MODE_PRIVATE);

        //Stop first time helper pop-ups from appearing
        sharedPreferences.edit().putBoolean("first-run", false).commit();
        sharedPreferences.edit().putBoolean("first-run-qm-help", false).commit();
        sharedPreferences.edit().putBoolean("first-run-aq-help", false).commit();
        sharedPreferences.edit().putBoolean("first-run-eq-help", false).commit();

        //Load example tags with their questions into the app
        ViewInteraction loadExampleQuestionsButton = onView(
                allOf(withId(R.id.load_example_questions_button), withText("Load Example Questions"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0)));
        loadExampleQuestionsButton.perform(scrollTo(), click());

        //Confirm the example tags with their questions are to be loaded into the app
        ViewInteraction confirmLoadExampleQuestions = onView(
                allOf(withClassName(is("androidx.appcompat.widget.AppCompatButton")), withText("Yes"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.buttonPanel),
                                        0),
                                3)));
        confirmLoadExampleQuestions.perform(scrollTo(), click());

        //Go to the edit question form for the selected question within the question manager
        DataInteraction goToEditQuestionFormForSelectedQuest = onData(anything())
                .inAdapterView(allOf(withId(R.id.question_list_view),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(7);
        goToEditQuestionFormForSelectedQuest.perform(click());

        //Change answer 3 text from "James Gosling" to "James Glossy"
        ViewInteraction changeAnswer3 = onView(
                allOf(withId(R.id.editTextAnswer3Edit), withText("James Gosling"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                1)));
        changeAnswer3.perform(scrollTo(), replaceText("James Glossy"));

        //Close the text input pop-up for answer 3
        ViewInteraction closeTextInput = onView(
                allOf(withId(R.id.editTextAnswer3Edit), withText("James Glossy"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                1),
                        isDisplayed()));
        closeTextInput.perform(closeSoftKeyboard());

        //Update the edited question within the database
        ViewInteraction updateQuestionButton = onView(
                allOf(withId(R.id.submitButtonEdit), withText("Submit Button"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        8),
                                0)));
        updateQuestionButton.perform(scrollTo(), click());
    }

    //Test a tag with it's questions can be shared
    @Test
    public void testShareTagAndQuestions(){
        //Clear database for test
        QuizDatabase db = QuizDatabase.getInstance(getApplicationContext());
        db.clearAllTables();

        //Initialize shared preferences
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.mob_dev_portfolio",
                Context.MODE_PRIVATE);

        //Stop first time helper pop-ups from appearing
        sharedPreferences.edit().putBoolean("first-run", false).commit();
        sharedPreferences.edit().putBoolean("first-run-qm-help", false).commit();
        sharedPreferences.edit().putBoolean("first-run-aq-help", false).commit();
        sharedPreferences.edit().putBoolean("first-run-eq-help", false).commit();

        //Load example tags with their questions into the app
        ViewInteraction loadExampleQuestionsButton = onView(
                allOf(withId(R.id.load_example_questions_button), withText("Load Example Questions"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0)));
        loadExampleQuestionsButton.perform(scrollTo(), click());

        //Confirm the example tags with their questions are to be loaded into the app
        ViewInteraction confirmLoadExampleQuestions = onView(
                allOf(withClassName(is("androidx.appcompat.widget.AppCompatButton")), withText("Yes"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.buttonPanel),
                                        0),
                                3)));
        confirmLoadExampleQuestions.perform(scrollTo(), click());

        //Test the tag and it's questions can be shared
        ViewInteraction shareTagAndQuestionsButton = onView(
                allOf(withId(R.id.shareQuestionsButton), withContentDescription("Share question list"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        shareTagAndQuestionsButton.perform(click());
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
