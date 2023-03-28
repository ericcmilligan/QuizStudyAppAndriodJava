package com.example.mob_dev_portfolio.Fragments.QuizGame;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.databinding.FragmentQcListBinding;
import com.example.mob_dev_portfolio.databinding.FragmentQuizGameBinding;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


public class QuizGameFragment extends Fragment {

    FragmentQuizGameBinding binding;

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

    //Save default text color
    private ColorStateList textColorDefaultRb;

    //Get questions from database and save into a list
    private List<Question> questionsList;

    //State variables for quiz game
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score;
    private Boolean answered;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Initialize the database
        QuizDatabase db = QuizDatabase.getInstance(getActivity().getApplicationContext());

        //Get the Tag ID passed from the bundle
        String bundleReceivedString = this.getArguments().toString();
        String bundleInt = bundleReceivedString.replaceAll("\\D+","");
        int tagID = Integer.parseInt(bundleInt);

        //Populate question list with questions for passed tag if there are questions for the tag
        if(tagID != 0){
            questionsList = db.questionDao().getQuestionsByTagID(tagID);
        } else {
            Toast.makeText(getContext(), "Please make sure you have started a quiz for a valid" +
                    " tag with more than one question!", Toast.LENGTH_LONG).show();
        }

        //Get question count total
        questionCountTotal = questionsList.size();

        //Randomize question output
        Collections.shuffle(questionsList);

        showNextQuestion(view);

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!answered){
                    if(rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()){
                        checkAnswer(view);
                    } else {
                        Toast.makeText(getContext(), "Please select an answer", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
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
       } else {
           finishQuiz(view);
       }
    }

    private void checkAnswer(View view) {
        answered = true;

        //Get selected answer and the index of the selected answer
        RadioButton rbSelected = view.findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;

        //Check if the answer is correct
        if(answerNr == currentQuestion.getCorrectAnswerID()){
            score = score + 1;
            textViewScore.setText("Score: " + score);
        }

        showSolution();
    }

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
        } else if (score >= highScore & score > 0){
            //Else update the high-score if the game score is more/the same as the current high-score saved
            //for the tag
            Toast.makeText(getContext(), "Well done, high score achieved!",
                    Toast.LENGTH_SHORT).show();
            db.highScoreDao().updateHighScoreByTagID(tagID, score);
        } else {
            //Else inform the user that they did not beat or match the high-score
            Toast.makeText(getContext(), "Unlucky! high score not achieved, better luck next time",
                    Toast.LENGTH_SHORT).show();
        }

        Navigation.findNavController(view).navigate(R.id.action_nav_quiz_game_to_nav_quiz_category);
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}