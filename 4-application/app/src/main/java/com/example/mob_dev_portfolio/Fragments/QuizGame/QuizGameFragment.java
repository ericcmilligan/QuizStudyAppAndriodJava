package com.example.mob_dev_portfolio.Fragments.QuizGame;

import android.app.NotificationManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mob_dev_portfolio.Database.QuizDatabase;
import com.example.mob_dev_portfolio.Entities.Answer;
import com.example.mob_dev_portfolio.Entities.Highscore;
import com.example.mob_dev_portfolio.Entities.Question;
import com.example.mob_dev_portfolio.Entities.Tag;
import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.databinding.FragmentQcListBinding;
import com.example.mob_dev_portfolio.databinding.FragmentQuizGameBinding;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


/**
 * A fragment that contains the quiz game logic.
 */
public class QuizGameFragment extends Fragment {

    FragmentQuizGameBinding binding;

    //Set up countdown variable
    private static final long COUNTDOWN_IN_MILLIS = 30000;

    //Set up the variables for the various views in the quiz game
    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCountdown;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private Button buttonConfirmNext;

    //Save data on instance change variables
    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";

    //Save default text and countdown color
    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    //Get questions from database and save into a list
    private ArrayList<Question> questionsList;

    //State variables for quiz game
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score;
    private Boolean answered = false;

    //Notification variables
    private NotificationManager notificationManager;
    public static final String CHANNEL_GAME_ID = "channel2";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set up notification manager
        notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        binding = FragmentQuizGameBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_quiz_game, container, false);

        //Assign the variables to the quiz game views
        textViewQuestion = view.findViewById(R.id.quiz_game_question_text);
        textViewScore = view.findViewById(R.id.quiz_game_score_text);
        textViewQuestionCount = view.findViewById(R.id.quiz_game_question_count_text);
        textViewCountdown = view.findViewById(R.id.quiz_game_countdown_text);
        rbGroup = view.findViewById(R.id.quiz_game_radio_group);
        rb1 = view.findViewById(R.id.quiz_game_radio_group_option1);
        rb2 = view.findViewById(R.id.quiz_game_radio_group_option2);
        rb3 = view.findViewById(R.id.quiz_game_radio_group_option3);
        rb4 = view.findViewById(R.id.quiz_game_radio_group_option4);
        buttonConfirmNext = view.findViewById(R.id.quiz_game_confirm_button);

        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultCd = textViewCountdown.getTextColors();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Let the user know it is a scroll screen
        Toast.makeText(getContext(), "Please scroll to confirm if you can not see the button",
                Toast.LENGTH_LONG).show();

        //Initialize the database
        QuizDatabase db = QuizDatabase.getInstance(getActivity().getApplicationContext());

        //Get the Tag ID passed from the bundle
        String bundleReceivedString = this.getArguments().toString();
        String bundleInt = bundleReceivedString.replaceAll("\\D+","");
        int tagID = Integer.parseInt(bundleInt);

        //Populate question list with questions for passed tag if there are questions for the tag
        if(tagID != 0){
            questionsList = (ArrayList<Question>) db.questionDao().getQuestionsByTagID(tagID);
        } else {
            Toast.makeText(getContext(), "Please make sure you have started a quiz for a valid" +
                    " tag with more than one question!", Toast.LENGTH_LONG).show();
        }

        if(savedInstanceState == null){
            //Get question count total
            questionCountTotal = questionsList.size();

            //Randomize question output
            Collections.shuffle(questionsList);

            showNextQuestion(view);
        } else {
            questionsList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCountTotal = questionsList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionsList.get(questionCounter - 1);
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);

            if(!answered){
                startCountDown(view);
            } else {
                updateCountDownText();
                showSolution();
            }
        }

        //When the user chooses to go to the next question check if the current question is answered
        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                //If not already answered and a radio button is checked, then check the answer selected
                if(!answered){
                    if(rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()){
                        checkAnswer(view);
                    } else {
                        //Else ask the user to select an answer
                        Toast.makeText(getContext(), "Please select an answer", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    //Once the answer is reported as correct/incorrect and the answer has been revealed
                    //go to the next question
                    showNextQuestion(view);
                }
            }
        });
    }

    private void showNextQuestion(View view){

        //Initialize the database
        QuizDatabase db = QuizDatabase.getInstance(getActivity().getApplicationContext());

        //Set radio button's to default text color on next question
       rb1.setTextColor(textColorDefaultRb);
       rb2.setTextColor(textColorDefaultRb);
       rb3.setTextColor(textColorDefaultRb);
       rb4.setTextColor(textColorDefaultRb);

       //Clear checkmarks on radio buttons
       rbGroup.clearCheck();

       //Show questions until question counter is above question count total
       if(questionCounter < questionCountTotal){
           currentQuestion = questionsList.get(questionCounter);

           textViewQuestion.setText(currentQuestion.getTitle());

           //Get answers for current question and save into a list.
           List<Answer> questionAnswers = db.answerDao().getAllAnswersForQuestion(currentQuestion.getQuestionID());

           rb1.setText(questionAnswers.get(0).getText());
           rb2.setText(questionAnswers.get(1).getText());
           rb3.setText(questionAnswers.get(2).getText());
           rb4.setText(questionAnswers.get(3).getText());

           //Increment question counter
           questionCounter = questionCounter + 1;

           textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);

           answered = false;
           buttonConfirmNext.setText("Confirm");

           timeLeftInMillis  = COUNTDOWN_IN_MILLIS;
           startCountDown(view);
       } else {
           finishQuiz(view);
       }
    }

    //Initialize and start the countdown timer
    private void startCountDown(View view) {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer(view);
            }
        }.start();
    }

    //Update the countdown timer text view text to the time remaining
    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);

        textViewCountdown.setText(timeFormatted);

        //Set the text colour to red when there is less than 10 seconds remaining on the clock
        if(timeLeftInMillis < 10000){
            textViewCountdown.setTextColor(Color.RED);
        } else {
            textViewCountdown.setTextColor(textColorDefaultCd);
        }
    }

    private void checkAnswer(View view) {
        answered = true;

        countDownTimer.cancel();

        //Get selected answer and the index of the selected answer
        RadioButton rbSelected = view.findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;

        //Check if the answer is correct
        if(answerNr == currentQuestion.getCorrectAnswerID()){
            score = score + 1;
            textViewScore.setText("Score: " + score);

            MediaPlayer correctAnswerSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.correct);
            if(correctAnswerSound != null) {
                correctAnswerSound.start();
                if(!correctAnswerSound.isPlaying()) {
                    correctAnswerSound.release();
                }
            }
        } else {
            //Else play an incorrect answer sound effect
            MediaPlayer incorrectAnswerSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.incorrect);
            if(incorrectAnswerSound != null){
                incorrectAnswerSound.start();
                if(!incorrectAnswerSound.isPlaying()){
                    incorrectAnswerSound.release();
                }
            }
        }

        //Proceed to show the solution
        showSolution();
    }

    //Show the correct answer of the question to the user
    private void showSolution(){
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);

        switch(currentQuestion.getCorrectAnswerID()){
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 1 is correct");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 2 is correct");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 3 is correct");
                break;
            case 4:
                rb4.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 4 is correct");
                break;
        }

        if(questionCounter < questionCountTotal){
            buttonConfirmNext.setText("Next");
        } else {
            buttonConfirmNext.setText("Finish");
        }
    }

    //Finish the quiz and proceed to the quiz replay screen
    private void finishQuiz(View view) {
        //Get the Tag ID passed from the bundle
        String bundleReceivedString = this.getArguments().toString();
        String bundleInt = bundleReceivedString.replaceAll("\\D+","");
        int tagID = Integer.parseInt(bundleInt);

        //Initialize the database
        QuizDatabase db = QuizDatabase.getInstance(getActivity().getApplicationContext());
        Integer highScore =  db.highScoreDao().getHighScorePointsByTagID(tagID);

        //If high-score is null create a new record for high-score for this tag
        if(highScore == null){
            db.highScoreDao().insertAll(
                    new Highscore(tagID, score, LocalDateTime.now())
            );
            Toast.makeText(getContext(), "High score created, do try again!",
                    Toast.LENGTH_SHORT).show();
        } else if (score > highScore & score > 0){
            //Else update the high-score if the game score is more than the current high-score saved
            //for the tag and send a notification alerting the user
            Toast.makeText(getContext(), "Well done, high score achieved!",
                    Toast.LENGTH_SHORT).show();

            db.highScoreDao().updateHighScoreByTagID(tagID, score, LocalDateTime.now());

            sendOnChannel2(view, db.highScoreDao().getHighScoreByTagID(tagID), db.tagDao().getTagByID(tagID));
        } else if (score == highScore & score > 0){
            //Else if the current score is the same as the previous high-score, inform the user
            Toast.makeText(getContext(), "Achieved the same high-score!",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            //Else inform the user that they did not beat or match the high-score
            Toast.makeText(getContext(), "Unlucky! high score not achieved, better luck next time",
                    Toast.LENGTH_SHORT).show();
        }

        //Put the score and tag ID into a bundle to be accessed within the quiz replay fragment
        Bundle bundle = new Bundle();
        bundle.putInt("score", score);
        bundle.putInt("tagID", tagID);

        //Go the quiz replay screen and pass the bundle
        Navigation.findNavController(view).navigate(R.id.action_nav_quiz_game_to_nav_quiz_replay, bundle);
    }

    //Send the notifications on channel 2
    public void sendOnChannel2(View v, Highscore highscore, Tag tag){
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(requireContext(), CHANNEL_GAME_ID);

        notificationBuilder
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("High-score achieved for tag " + tag.getName() + " with " + highscore.getScore() + " points!")
                .setContentText("Successfully achieved high-score for tag " + tag.getName())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setChannelId(CHANNEL_GAME_ID)
                .setAutoCancel(true);

        notificationManager.notify(0, notificationBuilder.build());
    }

    public void onDestroyView() {
        super.onDestroyView();
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
        binding = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        if(score == 0){
            outState.putInt(KEY_SCORE, 0);
        } else {
            outState.putInt(KEY_SCORE, score);
        }
        outState.putInt(KEY_QUESTION_COUNT,questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionsList);
    }
}