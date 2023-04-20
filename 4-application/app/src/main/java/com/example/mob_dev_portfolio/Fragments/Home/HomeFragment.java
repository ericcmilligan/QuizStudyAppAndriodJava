package com.example.mob_dev_portfolio.Fragments.Home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.mob_dev_portfolio.Database.QuizDatabase;
import com.example.mob_dev_portfolio.Entities.Answer;
import com.example.mob_dev_portfolio.Entities.Highscore;
import com.example.mob_dev_portfolio.Entities.Question;
import com.example.mob_dev_portfolio.Entities.Tag;
import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A fragment to provide navigation to the different screens in the app
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    //Initialize shared preferences
    SharedPreferences sharedPreferences = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Load project shared preferences file into the shared preferences variable
        sharedPreferences = getContext().getSharedPreferences("com.example.mob_dev_portfolio",
                Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Button set up with a onclick listener for loading two example tags with questions to provide
        //example input for the quiz
        Button loadExampleQuestionsButton = binding.loadExampleQuestionsButton;
        loadExampleQuestionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                AlertDialog.Builder alert = new AlertDialog.Builder(getContext(),
                        R.style.Theme_Mobdevportfolio);

                alert.setTitle("Load example questions");

                alert.setMessage("Do you want to load the example tags: Android Development and Java Development with their questions " +
                        "into the app? ");

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try{
                            QuizDatabase db = QuizDatabase.getInstance(getActivity().getApplicationContext());

                            Tag androidDevelopmentTag =  db.tagDao().getTagByName("Android-Development");
                            Tag javaDevelopmentTag = db.tagDao().getTagByName("Java-Development");

                            //Add android development tag and its questions if it does not exist
                            if(androidDevelopmentTag == null){
                                db.tagDao().insertAll(new Tag("Android-Development"));

                                Integer exampleTag1ID = db.tagDao().getTagIDByName("Android-Development");

                                db.questionDao().insertAll(new Question(exampleTag1ID, null,
                                                "Which tool is used to display information for a short period of time to the user?"),
                                        new Question(exampleTag1ID, null,
                                                "What is the name for a piece of an activity which enables more modular activity design?"),
                                        new Question(exampleTag1ID, null,
                                                "What is the name for a message that Android displays outside of the app?"),
                                        new Question(exampleTag1ID, null,
                                                "What is the name for the widget that is a more advanced version of a ListView?"));

                                List<Question> exampleTag1Questions = db.questionDao().getQuestionsByTagID(exampleTag1ID);

                                db.answerDao().insertAll(
                                        new Answer(exampleTag1Questions.get(0).getQuestionID(), "Banana"),
                                        new Answer(exampleTag1Questions.get(0).getQuestionID(), "Toast"),
                                        new Answer(exampleTag1Questions.get(0).getQuestionID(), "Chocolate"),
                                        new Answer(exampleTag1Questions.get(0).getQuestionID(), "Crisp"),
                                        new Answer(exampleTag1Questions.get(1).getQuestionID(), "Parchment"),
                                        new Answer(exampleTag1Questions.get(1).getQuestionID(), "Snippet"),
                                        new Answer(exampleTag1Questions.get(1).getQuestionID(), "Fragment"),
                                        new Answer(exampleTag1Questions.get(1).getQuestionID(), "Component"),
                                        new Answer(exampleTag1Questions.get(2).getQuestionID(), "Notification"),
                                        new Answer(exampleTag1Questions.get(2).getQuestionID(), "Alert"),
                                        new Answer(exampleTag1Questions.get(2).getQuestionID(), "Notice"),
                                        new Answer(exampleTag1Questions.get(2).getQuestionID(), "Report"),
                                        new Answer(exampleTag1Questions.get(3).getQuestionID(), "RecordView"),
                                        new Answer(exampleTag1Questions.get(3).getQuestionID(), "RecyclerView"),
                                        new Answer(exampleTag1Questions.get(3).getQuestionID(), "ReadingView"),
                                        new Answer(exampleTag1Questions.get(3).getQuestionID(), "CatalogueView")
                                );


                                db.questionDao().updateQuestionCorrectAnswer(2, exampleTag1Questions.get(0).getQuestionID());
                                db.questionDao().updateQuestionCorrectAnswer(3, exampleTag1Questions.get(1).getQuestionID());
                                db.questionDao().updateQuestionCorrectAnswer(1,exampleTag1Questions.get(2).getQuestionID());
                                db.questionDao().updateQuestionCorrectAnswer(2,exampleTag1Questions.get(3).getQuestionID());

                                //Initialize high-score for tag if not created
                                Tag createdTag = db.tagDao().getTagByID(exampleTag1ID);
                                Integer highScore =  db.highScoreDao().getHighScorePointsByTagID(createdTag.getTagID());

                                //If high-score is null create a new record for high-score for this tag
                                if(highScore == null){
                                    db.highScoreDao().insertAll(
                                            new Highscore(createdTag.getTagID(), 0, LocalDateTime.now())
                                    );
                                    Toast.makeText(getContext(), "High score initialized for tag: " + createdTag.getName(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                            //Add java development tag and its questions if it does not exist
                            if (javaDevelopmentTag == null){
                                db.tagDao().insertAll(new Tag("Java-Development"));

                                Integer exampleTag2ID = db.tagDao().getTagIDByName("Java-Development");

                                db.questionDao().insertAll(new Question(exampleTag2ID, null,
                                                "Which company originally developed java?"),
                                        new Question(exampleTag2ID, null,
                                                "When was java first released?"),
                                        new Question(exampleTag2ID, null,
                                                "Java is named after?"),
                                        new Question(exampleTag2ID, null,
                                                "Which person originally designed java?"));

                                List<Question> exampleTag2Questions = db.questionDao().getQuestionsByTagID(exampleTag2ID);

                                db.answerDao().insertAll(
                                        new Answer(exampleTag2Questions.get(0).getQuestionID(), "Sun Microsystems"),
                                        new Answer(exampleTag2Questions.get(0).getQuestionID(), "Oracle"),
                                        new Answer(exampleTag2Questions.get(0).getQuestionID(), "Microsoft"),
                                        new Answer(exampleTag2Questions.get(0).getQuestionID(), "Google"),
                                        new Answer(exampleTag2Questions.get(1).getQuestionID(), "1990"),
                                        new Answer(exampleTag2Questions.get(1).getQuestionID(), "1988"),
                                        new Answer(exampleTag2Questions.get(1).getQuestionID(), "1997"),
                                        new Answer(exampleTag2Questions.get(1).getQuestionID(), "1995"),
                                        new Answer(exampleTag2Questions.get(2).getQuestionID(), "Beer"),
                                        new Answer(exampleTag2Questions.get(2).getQuestionID(), "Coffee"),
                                        new Answer(exampleTag2Questions.get(2).getQuestionID(), "Tea"),
                                        new Answer(exampleTag2Questions.get(2).getQuestionID(), "Cider"),
                                        new Answer(exampleTag2Questions.get(3).getQuestionID(), "Daniel Honegger"),
                                        new Answer(exampleTag2Questions.get(3).getQuestionID(), "Jane Owen"),
                                        new Answer(exampleTag2Questions.get(3).getQuestionID(), "James Gosling"),
                                        new Answer(exampleTag2Questions.get(3).getQuestionID(), "José Silva")
                                );

                                db.questionDao().updateQuestionCorrectAnswer(1, exampleTag2Questions.get(0).getQuestionID());
                                db.questionDao().updateQuestionCorrectAnswer(4, exampleTag2Questions.get(1).getQuestionID());
                                db.questionDao().updateQuestionCorrectAnswer(2, exampleTag2Questions.get(2).getQuestionID());
                                db.questionDao().updateQuestionCorrectAnswer(3, exampleTag2Questions.get(3).getQuestionID());

                                //Initialize high-score for tag if not created
                                Tag createdTag = db.tagDao().getTagByID(exampleTag2ID);
                                Integer highScore =  db.highScoreDao().getHighScorePointsByTagID(createdTag.getTagID());

                                //If high-score is null create a new record for high-score for this tag
                                if(highScore == null){
                                    db.highScoreDao().insertAll(
                                            new Highscore(createdTag.getTagID(), 0, LocalDateTime.now())
                                    );
                                    Toast.makeText(getContext(), "High score initialized for tag: " + createdTag.getName(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                            //If both sets are already loaded into the app then alert the user
                            if(!(androidDevelopmentTag == null) & !(javaDevelopmentTag == null)){
                                Toast.makeText(getContext(),
                                        "Example tags with questions are already present in the app",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } catch(Exception e){
                            //Show the user why the error occurred
                            Toast.makeText(getContext(),
                                            "Error: " + e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(getContext(),
                                "The example tags and their questions have been loaded into the app",
                                Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_question_manager);
                    }
                });


                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        });


        //Button set up with a onclick listener to take the user to the question manager screen
        //on click
        Button questionManagerButton = binding.questionManagerButton;
        questionManagerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_question_manager);
            }
        });

        //Button set up with a onclick listener to take the user to the question category fragment
        //on click
        Button playQuizButton = binding.playQuizButton;
        playQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_quiz_category);
            }
        });

        //Button set up with a onclick listener to take the user to the high score fragment
        //on click
        Button highScoreButton = binding.highScoreButton;
        highScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_high_score);
            }
        });

        //Button set up with a onclick listener to take the user to the question manager tutorial fragment
        //on click
        Button tutorialButton = binding.tutorialButton;
        tutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_question_manager_tutorial);
            }
        });

        //Button set up with a onclick listener to assist the user by providing a toast with
        //explanation of the functionality for this screen.
        FloatingActionButton homeHelperButton = binding.homeHelperButton;

        homeHelperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                Toast.makeText(getActivity().getApplicationContext(), "You are on the home page click one of the buttons to" +
                                " go to a different section"
                        , Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Check if this is the first time the application has been installed
        if(sharedPreferences.getBoolean("first-run", true)) {
            //Then launch the pop-up asking the user if they would like to start the in-app tutorial
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext(),
                    androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);

            alert.setTitle("Will you like to launch the in-app tutorial?");

            //Go the in-app tutorial if the user clicks yes
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Navigation.findNavController(getView()).navigate(R.id.action_nav_home_to_nav_question_manager_tutorial);
                }
            });

            //If not cancel the pop-up
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Cancel
                }
            });

            alert.show();

            //Set first run to false after this method has finished
            sharedPreferences.edit().putBoolean("first-run", false).commit();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}