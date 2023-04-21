package com.example.mob_dev_portfolio.Fragments.QuestionManager;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mob_dev_portfolio.Database.QuizDatabase;
import com.example.mob_dev_portfolio.Entities.Answer;
import com.example.mob_dev_portfolio.Entities.Question;
import com.example.mob_dev_portfolio.Entities.Tag;
import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.databinding.FragmentQmListBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of questions.
 */
public class QMListFragment extends Fragment {


    FragmentQmListBinding binding;

    ArrayList<String> questionTitleList = new ArrayList<>();

    ArrayList<Question> questionsList = new ArrayList<>();

    ArrayAdapter adapter;

    ListView lv;

    private NotificationManager notificationManager;

    public static final String CHANNEL_DATABASE_ID = "channel1";


    public QMListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set up notification manager
        notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentQmListBinding.inflate(inflater, container, false);

        View view = inflater.inflate(R.layout.fragment_qm_list, container, false);

        //Set up button to allow user to add a question
        FloatingActionButton addQuestionButton = (FloatingActionButton) view.findViewById(R.id.addQuestionButton);

        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                Navigation.findNavController(v).navigate(R.id.action_nav_question_manager_to_nav_add_question);
            }
        });

        //Set up button to allow user to go back to the home-screen
        FloatingActionButton backToHomeButton = (FloatingActionButton) view.findViewById(R.id.backToHomeButton);

        backToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                Navigation.findNavController(v).navigate(R.id.action_nav_question_manager_to_nav_home);
            }
        });

        //Set up button for providing help to the user for the current screen
        FloatingActionButton qmHelperButton = (FloatingActionButton) view.findViewById(R.id.qmHelperButton);

        qmHelperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                Toast.makeText(getActivity().getApplicationContext(), "Change the tag through the drop-down list to view questions for a given tag"
                        , Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity().getApplicationContext(), "Scroll questions for the chosen tag in the list and click a question to go to the edit/delete form"
                        , Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity().getApplicationContext(), "Click the plus icon to add a new question"
                        , Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity().getApplicationContext(), "Use the pen icon floating button to update the selected tag's name"
                        , Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity().getApplicationContext(), "Use the trash icon floating button to delete the tag and its questions"
                        , Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Populate the list will all questions by default
        QuizDatabase db = QuizDatabase.getInstance(getActivity().getApplicationContext());

        questionsList = (ArrayList<Question>) db.questionDao().getAllQuestions();

        for (int i = 0; i < questionsList.size(); i++) {
            questionTitleList.add(i, questionsList.get(i).getTitle());
        }

        this.lv = (ListView) view.findViewById(R.id.question_list_view);

        this.adapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                questionTitleList);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //Getting question title from selected item and passing to edit fragment
                String selectedQuestionTitle = (String) adapter.getItem(position).toString();

                Integer questionID = db.questionDao().getQuestionIDByName(selectedQuestionTitle);

                //Put the question id into a bundle
                Bundle bundle = new Bundle();
                bundle.putInt("questionID", questionID);

                //Go to the edit fragment page using the bundle to pass the selected question's data
                Navigation.findNavController(v).navigate(R.id.action_nav_question_manager_to_nav_edit_question
                        , bundle);
            }
        });

        //Populate tag drop-down with tags in the system
        Spinner spinnerTagChooser = (Spinner)view.findViewById(R.id.tagChooserSpinner);

        ArrayAdapter<String> spinnerTagAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
        spinnerTagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTagChooser.setAdapter(spinnerTagAdapter);

        spinnerTagAdapter.add("All");

        List<Tag> tagsList = db.tagDao().getAllTags();

        for(Tag tag : tagsList){
            spinnerTagAdapter.addAll(tag.getName());
            spinnerTagAdapter.notifyDataSetChanged();
        }

        //Change the question list on drop-down change to list questions for chosen tag.
        spinnerTagChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerTagChooser.getSelectedItem().toString().equals("All")){
                    //If the tag is "all" then show all the questions in the database
                    questionTitleList.clear();

                    questionsList = (ArrayList<Question>) db.questionDao().getAllQuestions();

                    for (int i = 0; i < questionsList.size(); i++) {
                        questionTitleList.add(i, questionsList.get(i).getTitle());
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    //Else show the questions for the current selected tag
                    questionTitleList.clear();

                    Integer selectedTagID = db.tagDao().getTagIDByName(spinnerTagChooser.getSelectedItem().toString());

                    questionsList = (ArrayList<Question>) db.questionDao().getQuestionsByTagID(selectedTagID);

                    for (int i = 0; i < questionsList.size(); i++) {
                        questionTitleList.add(i, questionsList.get(i).getTitle());
                    }

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                    //Not needed as the spinner is populated with the tags all and default by default
            }
        });

        //Set up button for editing the name of a tag
        FloatingActionButton qmEditTagButton = (FloatingActionButton) view.findViewById(R.id.qmEditTagButton);

        qmEditTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                if(spinnerTagChooser.getSelectedItem() == null){
                    Toast.makeText(getContext(),
                                    "Please select a tag in the spinner first",
                                    Toast.LENGTH_SHORT)
                            .show();
                } else if (spinnerTagChooser.getSelectedItem().toString().equals("All")) {
                    Toast.makeText(getContext(),
                                    "Cannot edit the all tag name",
                                    Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Tag selectedTag =  db.tagDao().getTagByName(spinnerTagChooser.getSelectedItem().toString());

                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext(),
                            androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);

                    alert.setTitle("Edit tag");

                    alert.setMessage("Edit the tag: " + spinnerTagChooser.getSelectedItem());

                    // Set an EditText view to get user input
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
                                try{
                                    db.tagDao().updateTagNameByID(selectedTag.getTagID(), input.getText().toString());
                                } catch(Exception e){
                                    Toast.makeText(getContext(),
                                                    "Tag name must be unique, not saved",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }

                                //Send notification on tag name edited
                                Tag tagCreated = db.tagDao().getTagByName(input.getText().toString());
                                if(tagCreated != null){
                                    sendOnChannel1Notification2(v, db.tagDao().getTagByName(input.getText().toString()));
                                }

                                Navigation.findNavController(v).navigate(R.id.nav_question_manager);
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
            }
        });

        //Set up button for deleting a tag
        FloatingActionButton qmDeleteTagButton = (FloatingActionButton) view.findViewById(R.id.qmDeleteTagButton);
        qmDeleteTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                if(spinnerTagChooser.getSelectedItem() == null) {
                    Toast.makeText(getContext(),
                                    "Please select a tag in the spinner first to delete",
                                    Toast.LENGTH_SHORT)
                            .show();
                } else if (spinnerTagChooser.getSelectedItem().toString().equals("All")) {
                    Toast.makeText(getContext(),
                                    "Cannot delete the all tag",
                                    Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Tag selectedTag =  db.tagDao().getTagByName(spinnerTagChooser.getSelectedItem().toString());

                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext(),
                            androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);

                    alert.setTitle("Delete tag");

                    alert.setMessage("Are you sure you want to delete the tag: "
                            + spinnerTagChooser.getSelectedItem() + " and all it's questions?");


                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                                try{
                                    //Get all questions for the tag and delete the answers for each question and the question
                                    List<Question> questionsForTag = db.questionDao().getQuestionsByTagID(selectedTag.getTagID());
                                    for(Question question : questionsForTag){
                                        db.answerDao().deleteAnswersByQuestionID(question.getQuestionID());
                                        db.questionDao().deleteQuestionByID(question.getQuestionID());
                                    }
                                    //Delete the high-score and tag
                                    db.highScoreDao().deleteHighScoreByTagID(selectedTag.getTagID());
                                    db.tagDao().deleteTagByID(selectedTag.getTagID());

                                    Toast.makeText(getContext(),
                                                    selectedTag.getName() + " is deleted with all it's questions",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                    sendOnChannel1Notification1(v, selectedTag);
                                } catch(Exception e){
                                    //Tell the user why it can't be deleted
                                    Toast.makeText(getContext(),
                                                    "Cannot delete because",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                    Toast.makeText(getContext(),
                                                    e.toString(),
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                                Navigation.findNavController(v).navigate(R.id.nav_question_manager);
                            }
                        });


                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                        }
                    });

                    alert.show();
                }
            }
        });

        //Set up floating action button for sharing a tag and it's questions information through email on click
        FloatingActionButton shareQuestionListButton = (FloatingActionButton) view.findViewById(R.id.shareQuestionsButton);

        shareQuestionListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                //Set up variables to share in the email
                QuizDatabase db = QuizDatabase.getInstance(getContext());
                Tag selectedTag =  db.tagDao().getTagByName(spinnerTagChooser.getSelectedItem().toString());

                //Share all questions for the selected tag on button click
                if(selectedTag != null){
                    //Set the subject of the email to the tag name
                    String emailSubject = "Questions for tag: " + selectedTag.getName();

                    //Initialize the string builder to build the email text
                    StringBuilder emailText = new StringBuilder();
                    //Get all questions for the selected tag
                    List<Question> questionList =
                            db.questionDao().getQuestionsByTagID(selectedTag.getTagID());

                    //For each question in the questions list append the question's information to the string builder
                    for(int i = 0; i <  questionList.size(); i++){
                        if(i > 0){
                            emailText.append("\n\n");
                        }

                        emailText.append("Question Title: ");
                        emailText.append(questionsList.get(i).getTitle());
                        emailText.append("\n\n");
                        emailText.append("Question Answers: ");
                        List<Answer> questionAnswers =
                                db.answerDao().getAllAnswersForQuestion(questionsList.get(i).getQuestionID());
                        if (questionAnswers != null){
                            for (int z = 0; z <  questionAnswers.size(); z++) {
                                emailText.append("\n");
                                emailText.append("Option ").append(Integer.toString(z + 1)).append(": ");
                                emailText.append(questionAnswers.get(z).getText());
                            }
                        }
                        emailText.append("\n\n");
                        emailText.append("Correct Answer Number: ");
                        emailText.append(questionsList.get(i).getCorrectAnswerID().toString());
                    }

                    //Create email intent
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, emailText.toString());

                    //Open email intent
                    emailIntent.setType("message/rfc822");
                    v.getContext().startActivity(Intent.createChooser(emailIntent, "Choose an email client"));
                } else if(spinnerTagChooser.getSelectedItem() == "All") {
                    //Set the email subject title
                    String emailSubject = "All Questions Within The Test Quizzer App";

                    //Set the string builder for the email text
                    StringBuilder emailText = new StringBuilder();
                    List<Question> questionList =
                            db.questionDao().getAllQuestions();

                    //Append every question in the app with it's information to the email text
                    for(int i = 0; i <  questionList.size(); i++){
                        if(i > 0){
                            emailText.append("\n\n");
                        }

                        emailText.append("Question Title: ");
                        emailText.append(questionsList.get(i).getTitle());
                        emailText.append("\n\n");

                        String tagName = db.tagDao().getTagNameByID(questionsList.get(i).getTagID());
                        emailText.append("Question Tag: ");
                        emailText.append(tagName);

                        emailText.append("\n\n");
                        emailText.append("Question Answers: ");
                        List<Answer> questionAnswers =
                                db.answerDao().getAllAnswersForQuestion(questionsList.get(i).getQuestionID());
                        if (questionAnswers != null){
                            for (int z = 0; z <  questionAnswers.size(); z++) {
                                emailText.append("\n");
                                emailText.append("Option ").append(Integer.toString(z + 1)).append(": ");
                                emailText.append(questionAnswers.get(z).getText());
                            }
                        }

                        emailText.append("\n\n");
                        emailText.append("Correct Answer Number: ");
                        emailText.append(questionsList.get(i).getCorrectAnswerID().toString());
                    }

                    //Create email intent
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, emailText.toString());

                    //Open email intent
                    emailIntent.setType("message/rfc822");
                    v.getContext().startActivity(Intent.createChooser(emailIntent, "Choose an email client"));
                } else {
                    //Else alert the user to select a tag if not selected
                    Toast.makeText(getContext(), "Please select a tag to share it's questions",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Set up floating action button for exporting a tag and its questions
        FloatingActionButton exportQuestionListButton = (FloatingActionButton) view.findViewById(R.id.exportQuestionsToTextFileButton);

        exportQuestionListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                //Set up variables for exporting the tag and its questions to a text file
                QuizDatabase db = QuizDatabase.getInstance(getContext());
                Tag selectedTag =  db.tagDao().getTagByName(spinnerTagChooser.getSelectedItem().toString());

                //Export all questions for the selected tag on button click
                if(selectedTag != null) {
                    //Initialize the string builder to build the text file's text
                    StringBuilder fileText = new StringBuilder();
                    //Get all questions for the selected tag
                    List<Question> questionList =
                            db.questionDao().getQuestionsByTagID(selectedTag.getTagID());

                    //Tell the user the tag and it's questions are being exported to a text file
                    Toast.makeText(getContext(),
                            ("Exporting tag " + selectedTag.getName() +
                                    " and all its questions to a text file"),
                            Toast.LENGTH_SHORT).show();

                    fileText.append("Questions for tag: ").append(selectedTag.getName());
                    fileText.append("\n\n");

                    //For each question in the questions list append the question's information to the string builder
                    for(int i = 0; i <  questionList.size(); i++){
                        if(i > 0){

                            fileText.append("\n\n");
                        }

                        fileText.append("Question Title: ");
                        fileText.append(questionsList.get(i).getTitle());
                        fileText.append("\n\n");
                        fileText.append("Question Answers: ");
                        List<Answer> questionAnswers =
                                db.answerDao().getAllAnswersForQuestion(questionsList.get(i).getQuestionID());
                        if (questionAnswers != null){
                            for (int z = 0; z <  questionAnswers.size(); z++) {
                                fileText.append("\n");
                                fileText.append("Option ").append(Integer.toString(z + 1)).append(": ");
                                fileText.append(questionAnswers.get(z).getText());
                            }
                        }
                        fileText.append("\n\n");
                        fileText.append("Correct Answer Number: ");
                        fileText.append(questionsList.get(i).getCorrectAnswerID().toString());
                    }

                    //Save the file text into a text file
                    File downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File textFile = new File(downloadDirectory, (selectedTag.getName() + ".txt"));

                    //Try to write the text-file and catch a error if it occurs
                    try{
                        FileOutputStream outputStream = new FileOutputStream(textFile);
                        outputStream.write(fileText.toString().getBytes());
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(),("Error exporting to text file as: " +
                        e.getMessage()), Toast.LENGTH_SHORT).show();
                    }

                    //Check the text file has been successfully created
                    if(textFile.isFile()){
                        //Use the download manager to download the text file to the user's download directory
                        DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                        downloadManager.addCompletedDownload(textFile.getName(), textFile.getName(),
                                true, "text/plain",
                                textFile.getAbsolutePath(),textFile.length(),true);
                        Toast.makeText(getContext(),
                                (textFile.getName() + " exported to download directory")
                        ,Toast.LENGTH_LONG).show();
                    }
                } else if(spinnerTagChooser.getSelectedItem() == "All") {
                    //Set the string builder for the file text
                    StringBuilder fileText = new StringBuilder();

                    List<Question> questionList =
                            db.questionDao().getAllQuestions();

                    //Tell the user that all questions are being exported to a text file
                    Toast.makeText(getContext(),
                            ("Exporting all questions to a text file"),
                            Toast.LENGTH_SHORT).show();


                    fileText.append("All Questions Within The Test Quizzer App");
                    fileText.append("\n\n");

                    //Append every question in the app with it's information to the file text
                    for (int i = 0; i < questionList.size(); i++) {
                        if (i > 0) {
                            fileText.append("\n\n");
                        }

                        fileText.append("Question Title: ");
                        fileText.append(questionsList.get(i).getTitle());
                        fileText.append("\n\n");

                        String tagName = db.tagDao().getTagNameByID(questionsList.get(i).getTagID());
                        fileText.append("Question Tag: ");
                        fileText.append(tagName);

                        fileText.append("\n\n");
                        fileText.append("Question Answers: ");
                        List<Answer> questionAnswers =
                                db.answerDao().getAllAnswersForQuestion(questionsList.get(i).getQuestionID());
                        if (questionAnswers != null) {
                            for (int z = 0; z < questionAnswers.size(); z++) {
                                fileText.append("\n");
                                fileText.append("Option ").append(Integer.toString(z + 1)).append(": ");
                                fileText.append(questionAnswers.get(z).getText());
                            }
                        }

                        fileText.append("\n\n");
                        fileText.append("Correct Answer Number: ");
                        fileText.append(questionsList.get(i).getCorrectAnswerID().toString());
                    }


                    //Save the file text into a text file
                    File downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File textFile = new File(downloadDirectory, "AllQuestionsTextQuizzer.txt");

                    //Try to write the text-file and catch a error if it occurs
                    try{
                        FileOutputStream outputStream = new FileOutputStream(textFile);
                        outputStream.write(fileText.toString().getBytes());
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(),("Error exporting to text file as: " +
                                e.getMessage()), Toast.LENGTH_SHORT).show();
                    }

                    //Check the text file has been successfully created
                    if(textFile.isFile()){
                        //Use the download manager to download the text file to the user's download directory
                        DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                        downloadManager.addCompletedDownload(textFile.getName(), textFile.getName(),
                                true, "text/plain",
                                textFile.getAbsolutePath(),textFile.length(),true);
                        Toast.makeText(getContext(),
                                (textFile.getName() + " exported to download directory")
                                ,Toast.LENGTH_LONG).show();
                    }
                } else {
                    //Else alert the user to select a tag if not selected
                    Toast.makeText(getContext(), "Please select a tag to export its questions",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void sendOnChannel1Notification1(View v, Tag tag){
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(requireContext(), CHANNEL_DATABASE_ID);

        notificationBuilder
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Deleted tag " + tag.getName() + " and all its questions from the database")
                .setContentText("Successfully deleted tag " + tag.getName() + " and all its questions from the database")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setChannelId(CHANNEL_DATABASE_ID)
                .setAutoCancel(true);

        notificationManager.notify(0, notificationBuilder.build());
    }

    public void sendOnChannel1Notification2(View v, Tag tag){
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(requireContext(), CHANNEL_DATABASE_ID);

        notificationBuilder
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Edited tag name to " + tag.getName())
                .setContentText("Successfully edited tag name to " + tag.getName())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setChannelId(CHANNEL_DATABASE_ID)
                .setAutoCancel(true);

        notificationManager.notify(0, notificationBuilder.build());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}