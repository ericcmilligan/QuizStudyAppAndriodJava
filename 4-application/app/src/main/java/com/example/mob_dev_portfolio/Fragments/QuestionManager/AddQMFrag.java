package com.example.mob_dev_portfolio.Fragments.QuestionManager;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mob_dev_portfolio.Database.QuizDatabase;
import com.example.mob_dev_portfolio.Entities.Answer;
import com.example.mob_dev_portfolio.Entities.Highscore;
import com.example.mob_dev_portfolio.Entities.Question;
import com.example.mob_dev_portfolio.Entities.Tag;
import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.databinding.FragmentAddQuestionBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment to allow the user to add a question.
 */
public class AddQMFrag extends Fragment {

    //Set up variables for the fragment binding, form validation, notification manager and
    //notification channel
    private FragmentAddQuestionBinding binding;
    boolean isAllFieldsChecked = false;
    private NotificationManager notificationManager;
    public static final String CHANNEL_DATABASE_ID = "channel1";

    //Initialize shared preferences
    SharedPreferences sharedPreferences = null;

    public AddQMFrag() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set up notification manager
        notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
        //Load project shared preferences file into the shared preferences variable
        sharedPreferences = getContext().getSharedPreferences("com.example.mob_dev_portfolio",
                Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddQuestionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Initialize the app database
        QuizDatabase db = QuizDatabase.getInstance(getActivity().getApplicationContext());

        //Register the button used for question submission
        Button submitQMButton = (Button) root.findViewById(R.id.submitButton);

        //Submit question into the database
        submitQMButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                isAllFieldsChecked = CheckAllFields();

                //Check the fields for validation errors before submission into the database
                //and that the tag is not empty
                if(isAllFieldsChecked & binding.spinnerTagID.getSelectedItem() != null) {

                    //Insert the user's details for the question into the database
                    db.questionDao().insertAll(
                            new Question(
                                    db.tagDao().getTagIDByName(
                                            binding.spinnerTagID.getSelectedItem().toString()
                                    ),
                                    null,
                                    binding.editTextQuestionTitle.getText().toString())
                    );

                    //Insert the user's details for the answer into the database
                    db.answerDao().insertAll(
                            new Answer(
                                    db.questionDao().getQuestionIDByTitle(
                                            binding.editTextQuestionTitle.getText().toString()
                                    ),
                                    binding.editTextAnswer1.getText().toString()),
                            new Answer(db.questionDao().getQuestionIDByTitle(
                                    binding.editTextQuestionTitle.getText().toString()
                            ),
                                    binding.editTextAnswer2.getText().toString()),
                            new Answer(db.questionDao().getQuestionIDByTitle(
                                    binding.editTextQuestionTitle.getText().toString()
                            ),
                                    binding.editTextAnswer3.getText().toString()),
                            new Answer(db.questionDao().getQuestionIDByTitle(
                                    binding.editTextQuestionTitle.getText().toString()
                            ),
                                    binding.editTextAnswer4.getText().toString())
                    );

                    //After the answers have been submitted assign the correct answer for the question
                    //From the selected answer number in the spinner
                    if(binding.spinnerCorrectAnswerID.getSelectedItemPosition() == 0){
                        //If a correct answer is not selected use 1 as the default
                        db.questionDao().updateQuestionCorrectAnswer(
                                1
                                , db.questionDao().getQuestionIDByTitle(
                                        binding.editTextQuestionTitle.getText().toString()
                                ));
                    } else {
                        //Else after the answers have been submitted assign the correct answer for the question
                        //From the selected answer number in the spinner
                        db.questionDao().updateQuestionCorrectAnswer(
                                Integer.parseInt(binding.spinnerCorrectAnswerID.getSelectedItem().toString())
                                , db.questionDao().getQuestionIDByTitle(
                                        binding.editTextQuestionTitle.getText().toString()
                                ));
                    }

                    //Go back to the question manager screen and send notification on success
                    sendOnChannel1(v,
                            db.questionDao().getQuestionByTitle(
                                    binding.editTextQuestionTitle.getText().toString()),
                            db.tagDao().getTagByName(binding.spinnerTagID.getSelectedItem().toString()));

                    Navigation.findNavController(v).navigate(R.id.action_nav_add_question_to_nav_question_manager);
                } else {
                    Toast.makeText(getContext(), "Please make sure a valid tag is added or selected",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        //Allow the user to go back to the question manager when this button is clicked
        FloatingActionButton backToQMButton = (FloatingActionButton) root.findViewById(R.id.backToQMButton);

        backToQMButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                Navigation.findNavController(v).navigate(R.id.action_nav_add_question_to_nav_question_manager);
            }
        });

        //Provide explanation of the functionality for this screen to the user when this button is
        //clicked
        FloatingActionButton addQuestionHelperButton  = (FloatingActionButton) root.findViewById(R.id.addQuestionHelperButton);

        addQuestionHelperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                showAddQuestionHelpPopUp();
            }
        });

        return root;
    }

    private void showAddQuestionHelpPopUp() {
        //Show the user a pop-up with information on adding a question
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext(),
                androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);

        alert.setTitle("Adding A Question For A Tag Help");
        alert.setMessage(
                        "1.Once the form has been filled out for the question you can scroll " +
                        "down and click the submit button to submit." +
                        "\n\n" +
                        "2.You can add a new tag by pressing the add new tag button."

        );

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    //Validate the fields the user has inputted
    private boolean CheckAllFields() {
        //Register all the EditText fields with their IDs.
        EditText editTextQuestionTitle = (EditText) binding.editTextQuestionTitle;
        EditText editTextAnswer1 = (EditText) binding.editTextAnswer1;
        EditText editTextAnswer2 = (EditText) binding.editTextAnswer2;
        EditText editTextAnswer3 = (EditText) binding.editTextAnswer3;
        EditText editTextAnswer4 = (EditText) binding.editTextAnswer4;

        if (editTextQuestionTitle.length() <= 3) {
            editTextQuestionTitle.setError("This field is required to be over 3 characters");
            return false;
        } else if(editTextQuestionTitle.length() > 100){
            editTextQuestionTitle.setError("This field is not allowed to be over 100 characters");
            return false;
        }

        if (editTextAnswer1.length() == 0) {
            editTextAnswer1.setError("This field is required to be entered");
            return false;
        } else if(editTextAnswer1.length() > 20){
            editTextAnswer1.setError("This field is not allowed to be over 20 characters");
            return false;
        }

        if (editTextAnswer2.length() == 0) {
            editTextAnswer2.setError("This field is required to be entered");
            return false;
        } else if(editTextAnswer2.length() > 20){
            editTextAnswer2.setError("This field is not allowed to be over 20 characters");
            return false;
        }

        if (editTextAnswer3.length() == 0) {
            editTextAnswer3.setError("This field is required to be entered");
            return false;
        } else if(editTextAnswer3.length() > 20){
            editTextAnswer3.setError("This field is not allowed to be over 20 characters");
            return false;
        }

        if (editTextAnswer4.length() == 0) {
            editTextAnswer4.setError("This field is required to be entered");
            return false;
        } else if(editTextAnswer4.length() > 20){
            editTextAnswer4.setError("This field is not allowed to be over 20 characters");
            return false;
        }

        //after all validation is complete with no errors return true.
        return true;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //Populate the tag spinner with the tags present in the database
        QuizDatabase db = QuizDatabase.getInstance(getActivity().getApplicationContext());
        Spinner spinnerTagID = (Spinner)view.findViewById(R.id.spinnerTagID);

        ArrayAdapter<String> spinnerTagAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
        spinnerTagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTagID.setAdapter(spinnerTagAdapter);

        List<Tag> tagsList = db.tagDao().getAllTags();

        for(Tag tag : tagsList){
            spinnerTagAdapter.addAll(tag.getName());
            spinnerTagAdapter.notifyDataSetChanged();
        }

        Button addTagButton = (Button) view.findViewById(R.id.addTagButton);

        //Allow user to add a tag in a separate popup window
        addTagButton.setOnClickListener(new View.OnClickListener() {
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
                        androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);

                alert.setTitle("Add tag");
                alert.setMessage("Add a tag");

                //Set an EditText view to get user input
                EditText input = new EditText(getActivity().getApplicationContext());
                alert.setView(input);

                //Accept the user inputted tag on ok press if the name is not null and unique.
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (input.getText().length() == 0 || input.getText().length() > 30) {
                            Toast.makeText(getContext(),
                                            "Tag name must not be null or over 30 characters",
                                            Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            if (db.tagDao().getTagByName(input.getText().toString()) == null) {
                                    db.tagDao().insertAll(new Tag(input.getText().toString()));
                                    //Initialize high-score for tag if not created
                                    Tag createdTag = db.tagDao().getTagByName(input.getText().toString());
                                    Integer highScore = db.highScoreDao().getHighScorePointsByTagID(createdTag.getTagID());

                                    //If high-score is null create a new record for high-score for this tag
                                    if (highScore == null) {
                                        db.highScoreDao().insertAll(
                                                new Highscore(createdTag.getTagID(), 0, LocalDateTime.now())
                                        );
                                        Toast.makeText(getContext(), "High score initialized for tag: " + createdTag.getName(),
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    //Set tag selector to newly created tag
                                    spinnerTagAdapter.addAll(createdTag.getName());
                                    spinnerTagAdapter.notifyDataSetChanged();
                                    List<Tag> tagList = db.tagDao().getAllTags();
                                    int tagListLastIndex = tagList.size();
                                    try {
                                        binding.spinnerTagID.setSelection(tagListLastIndex);
                                        Toast.makeText(getContext(), "Set tag selector to newly created tag",
                                                Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Toast.makeText(getContext(), "Error occurred selecting latest tag as: ",
                                                Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getContext(), e.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getContext(),
                                                    "Tag name must be unique, not saved",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        }
                });

                //Allow the user to exit the add tag pop-up without adding a tag
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        });
    }

    //Send the notifications on channel 1
    public void sendOnChannel1(View v, Question question, Tag tag){
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(requireContext(), CHANNEL_DATABASE_ID);

        notificationBuilder
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Added question " + question.getTitle() + " to tag " + tag.getName())
                .setContentText("Successfully added question " + question.getTitle() + " to tag " + tag.getName())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setChannelId(CHANNEL_DATABASE_ID)
                .setAutoCancel(true);

        notificationManager.notify(0, notificationBuilder.build());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        //Show add question help pop up if it is the first time visiting the fragment after install
        if(sharedPreferences.getBoolean("first-run-aq-help", true)) {
            showAddQuestionHelpPopUp();
            //Set first run to false after this method has finished
            sharedPreferences.edit().putBoolean("first-run-aq-help", false).commit();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}