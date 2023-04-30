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
import com.example.mob_dev_portfolio.databinding.FragmentEditQuestionBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment to allow the user to edit a question.
 */
public class EditQMFrag extends Fragment {

    private FragmentEditQuestionBinding binding;
    boolean isAllFieldsChecked = false;
    private NotificationManager notificationManager;
    public static final String CHANNEL_DATABASE_ID = "channel1";

    //Initialize shared preferences
    SharedPreferences sharedPreferences = null;


    public EditQMFrag() {

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

        binding = FragmentEditQuestionBinding.inflate(inflater,
                container, false);
        View root = binding.getRoot();
        QuizDatabase db = QuizDatabase.getInstance(getActivity().getApplicationContext());

        //Get the question and it's answers from the selected question within the question manager
        String bundleReceivedString = this.getArguments().toString();
        String bundleInt = bundleReceivedString.replaceAll("\\D+","");
        Integer selectedQuestionID = Integer.parseInt(bundleInt);
        Question selectedQuestion = db.questionDao().getQuestionByID(selectedQuestionID);
        Integer highScoreValue = db.highScoreDao().getHighScorePointsByTagID(selectedQuestion.getTagID());

        //Set up floating action buttons for navigation and to provide help to the user
        FloatingActionButton backToQMButtonEdit = (FloatingActionButton) root.findViewById(R.id.backToQMButtonEdit);

        backToQMButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                Navigation.findNavController(v).navigate(R.id.action_nav_edit_question_to_nav_question_manager);
            }
        });

        //Set up the floating action button for providing information on the edit question screen on click
        FloatingActionButton editQuestionHelperButton  = (FloatingActionButton) root.findViewById(R.id.editQuestionHelperButton);

        editQuestionHelperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                showEditQuestionHelpPopUp();
            }
        });

        //Allow deletion of the question from the database
        FloatingActionButton deleteQuestionButton = binding.editQuestionDeleteButton;

        //Ask the user if they are sure they want to delete the record in the form of a pop-up
        deleteQuestionButton.setOnClickListener(new View.OnClickListener() {
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

                alert.setTitle("Are you sure you want to delete this question and it's related answers?");
                alert.setMessage(selectedQuestion.getTitle());

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Delete a point from the high-score as the question is deleted
                        if(highScoreValue != null){
                            db.highScoreDao().updateHighScoreByTagID(selectedQuestion.getTagID(),
                                    (highScoreValue - 1), LocalDateTime.now());
                            Toast.makeText(getContext(),
                                            "Point knocked off high score for tag: " +
                                                    db.tagDao().getTagNameByID(selectedQuestion.getTagID())
                                                    +  " as question deleted",
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                        //Delete the question and it's answers from the database
                        db.answerDao().deleteAnswersByQuestionID(selectedQuestionID);
                        db.questionDao().deleteQuestionByID(selectedQuestionID);
                        Navigation.findNavController(v).navigate(R.id.action_nav_edit_question_to_nav_question_manager);
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled
                    }
                });

                alert.show();
            }
        });

        //Populate tag spinner
        Spinner spinnerTagID = binding.spinnerTagEditID;

        ArrayAdapter<String> spinnerTagAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
        spinnerTagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTagID.setAdapter(spinnerTagAdapter);

        List<Tag> tagsList = db.tagDao().getAllTags();

        for(Tag tag : tagsList){
            spinnerTagAdapter.addAll(tag.getName());
            spinnerTagAdapter.notifyDataSetChanged();
        }

        List<Answer> getAnswersForSelectedQuestion =
                db.answerDao().getAllAnswersForQuestion(selectedQuestionID);
        int correctAnswerNumber = db.questionDao().getCorrectAnswerIDByQuestionID(selectedQuestionID)
                - 1;
        int questionTagID = selectedQuestion.getTagID() - 1;

        //Pre-populate fields with the existing data for the selected question
        Spinner spinnerTagEditID =  binding.spinnerTagEditID;
        EditText editTextQuestionTitleEdit = binding.editTextQuestionTitleEdit;
        EditText editTextAnswer1Edit =  binding.editTextAnswer1Edit;
        EditText editTextAnswer2Edit =  binding.editTextAnswer2Edit;
        EditText editTextAnswer3Edit =  binding.editTextAnswer3Edit;
        EditText editTextAnswer4Edit =  binding.editTextAnswer4Edit;
        Spinner spinnerCorrectAnswerEditID =  binding.spinnerCorrectAnswerEditID;

        spinnerTagEditID.setSelection(questionTagID);
        editTextQuestionTitleEdit.setText(selectedQuestion.getTitle());
        editTextAnswer1Edit.setText(getAnswersForSelectedQuestion.get(0).getText());
        editTextAnswer2Edit.setText(getAnswersForSelectedQuestion.get(1).getText());
        editTextAnswer3Edit.setText(getAnswersForSelectedQuestion.get(2).getText());
        editTextAnswer4Edit.setText(getAnswersForSelectedQuestion.get(3).getText());
        spinnerCorrectAnswerEditID.setSelection(correctAnswerNumber);


        //Register buttons for submission and adding a tag
        Button submitQMButtonEdit = (Button) root.findViewById(R.id.submitButtonEdit);
        Button addTagButtonEdit = (Button) root.findViewById(R.id.addTagButtonEdit);

        //Allow user to add a tag
        addTagButtonEdit.setOnClickListener(new View.OnClickListener() {
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

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (input.getText().length() == 0 || input.getText().length() > 30) {
                            Toast.makeText(getContext(),
                                            "Tag name must not be null or over 30 characters",
                                            Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            db.tagDao().insertAll(new Tag(input.getText().toString()));

                            //Initialize high-score for tag if not created
                            Tag createdTag = db.tagDao().getTagByName(input.getText().toString());
                            Integer highScore =  db.highScoreDao().getHighScorePointsByTagID(createdTag.getTagID());

                            //If high-score is null create a new record for high-score for this tag
                            if(highScore == null){
                                db.highScoreDao().insertAll(
                                        new Highscore(createdTag.getTagID(), 0, LocalDateTime.now())
                                );
                                Toast.makeText(getContext(), "High score initialized for tag: " + createdTag.getName(),
                                        Toast.LENGTH_SHORT).show();
                            }

                            //Set tag selector to newly created tag
                            spinnerTagAdapter.addAll(createdTag.getName());
                            spinnerTagAdapter.notifyDataSetChanged();
                            List<Tag> tagList =  db.tagDao().getAllTags();
                            int tagListLastIndex = tagList.size();
                            try{
                                binding.spinnerTagEditID.setSelection(tagListLastIndex);
                                Toast.makeText(getContext(), "Set tag selector to newly created tag",
                                        Toast.LENGTH_SHORT).show();
                            } catch(Exception e){
                                Toast.makeText(getContext(), "Error occurred selecting latest tag as: ",
                                        Toast.LENGTH_SHORT).show();
                                Toast.makeText(getContext(), e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        });

        //Update question in the database
        submitQMButtonEdit.setOnClickListener(new View.OnClickListener() {
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

                if(isAllFieldsChecked) {

                    if(questionTagID != spinnerTagID.getId() & highScoreValue != null ){
                        if(highScoreValue > 0){
                            db.highScoreDao().updateHighScoreByTagID(selectedQuestion.getTagID(),
                                    (highScoreValue - 1), LocalDateTime.now());
                            Toast.makeText(getContext(),
                                            "Point knocked off high score for tag: " +
                                                    db.tagDao().getTagNameByID(selectedQuestion.getTagID())
                                            +  " as question edited",
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }

                    db.questionDao().updateQuestion(
                                    selectedQuestionID,
                                    db.tagDao().getTagIDByName(
                                            binding.spinnerTagEditID.getSelectedItem().toString()),
                                    binding.editTextQuestionTitleEdit.getText().toString()
                    );


                    db.answerDao().updateAnswer(
                                    getAnswersForSelectedQuestion.get(0).getAnswerID(),
                                    getAnswersForSelectedQuestion.get(0).getQuestionID(),
                                    binding.editTextAnswer1Edit.getText().toString()
                    );

                    db.answerDao().updateAnswer(
                            getAnswersForSelectedQuestion.get(1).getAnswerID(),
                            getAnswersForSelectedQuestion.get(1).getQuestionID(),
                            binding.editTextAnswer2Edit.getText().toString()
                    );

                    db.answerDao().updateAnswer(
                            getAnswersForSelectedQuestion.get(2).getAnswerID(),
                            getAnswersForSelectedQuestion.get(2).getQuestionID(),
                            binding.editTextAnswer3Edit.getText().toString()
                    );

                    db.answerDao().updateAnswer(
                            getAnswersForSelectedQuestion.get(3).getAnswerID(),
                            getAnswersForSelectedQuestion.get(3).getQuestionID(),
                            binding.editTextAnswer4Edit.getText().toString()
                    );

                    //After the answers have been submitted assign the correct answer for the question
                    //From the selected answer number in the spinner
                    if(binding.spinnerCorrectAnswerEditID.getSelectedItemPosition() == 0){
                        //If a correct answer is not selected use 1 as the default
                        db.questionDao().updateQuestionCorrectAnswer(
                                1
                                , db.questionDao().getQuestionIDByName(
                                        binding.editTextQuestionTitleEdit.getText().toString()
                                ));
                    } else {
                        //Else after the answers have been submitted assign the correct answer for the question
                        //From the selected answer number in the spinner
                        db.questionDao().updateQuestionCorrectAnswer(
                                Integer.parseInt(binding.spinnerCorrectAnswerEditID.getSelectedItem().toString())
                                , db.questionDao().getQuestionIDByName(
                                        binding.editTextQuestionTitleEdit.getText().toString()
                                ));
                    }

                    //Go back to the question manager screen and send notification on success
                    sendOnChannel1(v,
                            db.questionDao().getQuestionByTitle(
                                    binding.editTextQuestionTitleEdit.getText().toString()),
                            db.tagDao().getTagByName(binding.spinnerTagEditID.getSelectedItem().toString()));

                    Navigation.findNavController(v).navigate(R.id.action_nav_edit_question_to_nav_question_manager);
                }
            }
        });

        return root;
    }

    private void showEditQuestionHelpPopUp() {
        //Show the user a pop-up with information on editing a question
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext(),
                androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);

        alert.setTitle("Editing A Question For A Tag Help");
        alert.setMessage(
                        "1.Once the question has been edited using the form you can scroll " +
                        "down and click the submit button to submit." +
                        "\n\n" +
                        "2.You can add a new tag by pressing the add new tag button." +
                        "\n\n" +
                        "3.You can click the trash icon to delete this question from the tag."
        );

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    private boolean CheckAllFields() {
        //Register all the EditText fields with their IDs.
        EditText editTextQuestionTitle = (EditText) binding.editTextQuestionTitleEdit;
        EditText editTextAnswer1 = (EditText) binding.editTextAnswer1Edit;
        EditText editTextAnswer2 = (EditText) binding.editTextAnswer2Edit;
        EditText editTextAnswer3 = (EditText) binding.editTextAnswer3Edit;
        EditText editTextAnswer4 = (EditText) binding.editTextAnswer4Edit;

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

        //after all validation return true.
        return true;
    }

    //Send the notifications on channel 1
    public void sendOnChannel1(View v, Question question, Tag tag){
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(requireContext(), CHANNEL_DATABASE_ID);

        notificationBuilder
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Edited question " + question.getTitle() + " for tag " + tag.getName())
                .setContentText("Successfully edited question " + question.getTitle() + " for tag " + tag.getName())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setChannelId(CHANNEL_DATABASE_ID)
                .setAutoCancel(true);

        notificationManager.notify(0, notificationBuilder.build());
    }

    //Hide the toolbar while in this fragment as we have a custom toolbar
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        //Show edit question help pop up if it is the first time visiting the fragment after install
        if(sharedPreferences.getBoolean("first-run-eq-help", true)) {
            showEditQuestionHelpPopUp();
            //Set first run to false after this method has finished
            sharedPreferences.edit().putBoolean("first-run-eq-help", false).commit();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}