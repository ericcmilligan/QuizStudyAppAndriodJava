package com.example.mob_dev_portfolio;


import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
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

import androidx.test.espresso.DataInteraction;
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
 * An espresso test suite for testing the quiz game section of the app
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class QuizGameTestSuite {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @BeforeClass
    public static void setupDatabaseForTests() {
        //Clear database for tests
        QuizDatabase db = QuizDatabase.getInstance(getApplicationContext());
        db.clearAllTables();
    }

    //Test playing a quiz game all the way through to the end screen
    @Test
    public void testPlayingQuizGame() {
        //Initialize shared preferences
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.mob_dev_portfolio",
                Context.MODE_PRIVATE);

        //Stop first time helper pop-ups from appearing
        sharedPreferences.edit().putBoolean("first-run", false).commit();
        sharedPreferences.edit().putBoolean("first-run-qm-help", false).commit();
        sharedPreferences.edit().putBoolean("first-run-aq-help", false).commit();
        sharedPreferences.edit().putBoolean("first-run-eq-help", false).commit();
        sharedPreferences.edit().putBoolean("first-run-qc-help", false).commit();
        sharedPreferences.edit().putBoolean("first-run-qr-help", false).commit();

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

        //Go back to the home screen
        ViewInteraction backToHomeButton = onView(
                allOf(withId(R.id.backToHomeButton), withContentDescription("Back to home button"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                0),
                        isDisplayed()));
        backToHomeButton.perform(click());

        //Play the quiz
        ViewInteraction playQuizButton = onView(
                allOf(withId(R.id.play_quiz_button), withText("Play Quiz"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                0)));
        playQuizButton.perform(scrollTo(), click());

        //Choose quiz category
        DataInteraction chooseQuizCategory = onData(anything())
                .inAdapterView(allOf(withId(R.id.tag_list_view),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(1);
        chooseQuizCategory.perform(click());

        //Start the quiz game
        ViewInteraction startQuiz = onView(
                allOf(withId(R.id.quiz_start_button), withText("Start Quiz"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        startQuiz.perform(click());

        //Choose option 3 to answer the current question
        ViewInteraction chooseAnswer3 = onView(
                allOf(withId(R.id.quiz_game_radio_group_option3)));
        chooseAnswer3.perform(scrollTo(), click());

        //Confirm answer
        ViewInteraction confirmAnswer = onView(
                allOf(withId(R.id.quiz_game_confirm_button), withText("Confirm"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        confirmAnswer.perform(scrollTo(), click());

        //Go the the next question
        ViewInteraction nextQuestion = onView(
                allOf(withId(R.id.quiz_game_confirm_button), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        nextQuestion.perform(scrollTo(), click());

        //Choose option 1 to answer the current question
        ViewInteraction chooseAnswer1 = onView(
                allOf(withId(R.id.quiz_game_radio_group_option1)));
        chooseAnswer1.perform(scrollTo(), click());

        //Confirm answer
        ViewInteraction confirmAnswer2 = onView(
                allOf(withId(R.id.quiz_game_confirm_button), withText("Confirm"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        confirmAnswer2.perform(scrollTo(), click());

        //Go the the next question
        ViewInteraction nextQuestion2 = onView(
                allOf(withId(R.id.quiz_game_confirm_button), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        nextQuestion2.perform(scrollTo(), click());

        //Choose option 2 to answer the current question
        ViewInteraction chooseAnswer2 = onView(
                allOf(withId(R.id.quiz_game_radio_group_option2)));
        chooseAnswer2.perform(scrollTo(), click());

        //Confirm answer
        ViewInteraction confirmAnswer3 = onView(
                allOf(withId(R.id.quiz_game_confirm_button), withText("Confirm"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        confirmAnswer3.perform(scrollTo(), click());

        //Go the the next question
        ViewInteraction nextQuestion3 = onView(
                allOf(withId(R.id.quiz_game_confirm_button), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        nextQuestion3.perform(scrollTo(), click());

        //Choose option 2 to answer the question again
        ViewInteraction chooseAnswer2Again = onView(
                allOf(withId(R.id.quiz_game_radio_group_option2)));
        chooseAnswer2Again.perform(scrollTo(), click());

        //Confirm answer
        ViewInteraction confirmAnswer4 = onView(
                allOf(withId(R.id.quiz_game_confirm_button), withText("Confirm"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        confirmAnswer4.perform(scrollTo(), click());

        //Finish the quiz
        ViewInteraction finishQuiz = onView(
                allOf(withId(R.id.quiz_game_confirm_button), withText("Finish"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        finishQuiz.perform(scrollTo(), click());
    }

    //Test playing a quiz game all the way through to the end screen and then replaying the
    //quiz
    @Test
    public void testReplayingQuizGame() {
        //Initialize shared preferences
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.mob_dev_portfolio",
                Context.MODE_PRIVATE);

        //Stop first time helper pop-ups from appearing
        sharedPreferences.edit().putBoolean("first-run", false).commit();
        sharedPreferences.edit().putBoolean("first-run-qm-help", false).commit();
        sharedPreferences.edit().putBoolean("first-run-aq-help", false).commit();
        sharedPreferences.edit().putBoolean("first-run-eq-help", false).commit();
        sharedPreferences.edit().putBoolean("first-run-qc-help", false).commit();
        sharedPreferences.edit().putBoolean("first-run-qr-help", false).commit();

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

        //Go back to the home screen
        ViewInteraction backToHomeButton = onView(
                allOf(withId(R.id.backToHomeButton), withContentDescription("Back to home button"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                0),
                        isDisplayed()));
        backToHomeButton.perform(click());

        //Play the quiz
        ViewInteraction playQuizButton = onView(
                allOf(withId(R.id.play_quiz_button), withText("Play Quiz"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                0)));
        playQuizButton.perform(scrollTo(), click());

        //Choose quiz category
        DataInteraction chooseQuizCategory = onData(anything())
                .inAdapterView(allOf(withId(R.id.tag_list_view),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(1);
        chooseQuizCategory.perform(click());

        //Start the quiz game
        ViewInteraction startQuiz = onView(
                allOf(withId(R.id.quiz_start_button), withText("Start Quiz"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        startQuiz.perform(click());

        //Choose option 3 to answer the current question
        ViewInteraction chooseAnswer3 = onView(
                allOf(withId(R.id.quiz_game_radio_group_option3)));
        chooseAnswer3.perform(scrollTo(), click());

        //Confirm answer
        ViewInteraction confirmAnswer = onView(
                allOf(withId(R.id.quiz_game_confirm_button), withText("Confirm"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        confirmAnswer.perform(scrollTo(), click());

        //Go the the next question
        ViewInteraction nextQuestion = onView(
                allOf(withId(R.id.quiz_game_confirm_button), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        nextQuestion.perform(scrollTo(), click());

        //Choose option 1 to answer the current question
        ViewInteraction chooseAnswer1 = onView(
                allOf(withId(R.id.quiz_game_radio_group_option1)));
        chooseAnswer1.perform(scrollTo(), click());

        //Confirm answer
        ViewInteraction confirmAnswer2 = onView(
                allOf(withId(R.id.quiz_game_confirm_button), withText("Confirm"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        confirmAnswer2.perform(scrollTo(), click());

        //Go the the next question
        ViewInteraction nextQuestion2 = onView(
                allOf(withId(R.id.quiz_game_confirm_button), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        nextQuestion2.perform(scrollTo(), click());

        //Choose option 2 to answer the current question
        ViewInteraction chooseAnswer2 = onView(
                allOf(withId(R.id.quiz_game_radio_group_option2)));
        chooseAnswer2.perform(scrollTo(), click());

        //Confirm answer
        ViewInteraction confirmAnswer3 = onView(
                allOf(withId(R.id.quiz_game_confirm_button), withText("Confirm"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        confirmAnswer3.perform(scrollTo(), click());

        //Go the the next question
        ViewInteraction nextQuestion3 = onView(
                allOf(withId(R.id.quiz_game_confirm_button), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        nextQuestion3.perform(scrollTo(), click());

        //Choose option 2 to answer the question again
        ViewInteraction chooseAnswer2Again = onView(
                allOf(withId(R.id.quiz_game_radio_group_option2)));
        chooseAnswer2Again.perform(scrollTo(), click());

        //Confirm answer
        ViewInteraction confirmAnswer4 = onView(
                allOf(withId(R.id.quiz_game_confirm_button), withText("Confirm"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        confirmAnswer4.perform(scrollTo(), click());

        //Finish the quiz
        ViewInteraction finishQuiz = onView(
                allOf(withId(R.id.quiz_game_confirm_button), withText("Finish"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        finishQuiz.perform(scrollTo(), click());

        //Click the replay button to replay the quiz
        ViewInteraction replayQuizButton = onView(
                allOf(withId(R.id.replay_quiz_button), withText("Replay Quiz"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                0)));
        replayQuizButton.perform(scrollTo(), click());

        //Start the quiz game
        ViewInteraction startQuizReplay = onView(
                allOf(withId(R.id.quiz_start_button), withText("Start Quiz"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        startQuizReplay.perform(click());

        //Choose option 3 to answer the current question
        ViewInteraction chooseAnswer3Replay = onView(
                allOf(withId(R.id.quiz_game_radio_group_option3)));
        chooseAnswer3Replay.perform(scrollTo(), click());

        //Confirm answer
        ViewInteraction confirmAnswerReplay = onView(
                allOf(withId(R.id.quiz_game_confirm_button), withText("Confirm"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        confirmAnswerReplay.perform(scrollTo(), click());

        //Go the the next question
        ViewInteraction nextQuestionReplay = onView(
                allOf(withId(R.id.quiz_game_confirm_button), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        nextQuestionReplay.perform(scrollTo(), click());

        //Choose option 1 to answer the current question
        ViewInteraction chooseAnswer1Replay = onView(
                allOf(withId(R.id.quiz_game_radio_group_option1)));
        chooseAnswer1Replay.perform(scrollTo(), click());

        //Confirm answer
        ViewInteraction confirmAnswer2Replay = onView(
                allOf(withId(R.id.quiz_game_confirm_button), withText("Confirm"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        confirmAnswer2Replay.perform(scrollTo(), click());

        //Go the the next question
        ViewInteraction nextQuestion2Replay = onView(
                allOf(withId(R.id.quiz_game_confirm_button), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        nextQuestion2Replay.perform(scrollTo(), click());

        //Choose option 2 to answer the current question
        ViewInteraction chooseAnswer2Replay = onView(
                allOf(withId(R.id.quiz_game_radio_group_option2)));
        chooseAnswer2Replay.perform(scrollTo(), click());

        //Confirm answer
        ViewInteraction confirmAnswer3Replay = onView(
                allOf(withId(R.id.quiz_game_confirm_button), withText("Confirm"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        confirmAnswer3Replay.perform(scrollTo(), click());

        //Go the the next question
        ViewInteraction nextQuestion3Replay = onView(
                allOf(withId(R.id.quiz_game_confirm_button), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        nextQuestion3Replay.perform(scrollTo(), click());

        //Choose option 2 to answer the question again
        ViewInteraction chooseAnswer2AgainReplay = onView(
                allOf(withId(R.id.quiz_game_radio_group_option2)));
        chooseAnswer2AgainReplay.perform(scrollTo(), click());

        //Confirm answer
        ViewInteraction confirmAnswer4Replay = onView(
                allOf(withId(R.id.quiz_game_confirm_button), withText("Confirm"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        confirmAnswer4Replay.perform(scrollTo(), click());

        //Finish the quiz
        ViewInteraction finishQuizReplay = onView(
                allOf(withId(R.id.quiz_game_confirm_button), withText("Finish"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        finishQuizReplay.perform(scrollTo(), click());

        //Quit the quiz
        ViewInteraction quitQuiz = onView(
                allOf(withId(R.id.quit_quiz_button), withText("Quit Quiz"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                0)));
        quitQuiz.perform(scrollTo(), click());
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
