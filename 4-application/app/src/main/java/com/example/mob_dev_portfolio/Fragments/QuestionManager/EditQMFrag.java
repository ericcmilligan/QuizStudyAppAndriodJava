package com.example.mob_dev_portfolio.Fragments.QuestionManager;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.mob_dev_portfolio.Entities.Question;
import com.example.mob_dev_portfolio.Entities.Tag;
import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.databinding.FragmentEditQuestionBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class EditQMFrag extends Fragment {

    private FragmentEditQuestionBinding binding;
    boolean isAllFieldsChecked = false;


    public EditQMFrag() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentEditQuestionBinding.inflate(inflater,
                container, false);
        View root = binding.getRoot();
        QuizDatabase db = QuizDatabase.getInstance(getActivity().getApplicationContext());

        //Get question and answers from selected question
        Bundle bundleReceived = this.getArguments();
        String bundleReceivedString = this.getArguments().toString();
        String bundleInt = bundleReceivedString.replaceAll("\\D+","");
        Integer selectedQuestionID = Integer.parseInt(bundleInt);
        Question selectedQuestion = db.questionDao().getQuestionByID(selectedQuestionID);

        //Set up floating action buttons for navigation and help
        FloatingActionButton backToQMButtonEdit = (FloatingActionButton) root.findViewById(R.id.backToQMButtonEdit);

        backToQMButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_edit_question_to_nav_question_manager);
            }
        });

        FloatingActionButton editQuestionHelperButton  = (FloatingActionButton) root.findViewById(R.id.editQuestionHelperButton);

        editQuestionHelperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Fill out the form and scroll down to click submit to edit a question"
                        , Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity().getApplicationContext(), "Click the trash icon to delete this question"
                        , Toast.LENGTH_SHORT).show();
            }
        });

        //Allow deletion of the question
        FloatingActionButton deleteQuestionButton = binding.editQuestionDeleteButton;

        deleteQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext(),
                        R.style.Theme_Mobdevportfolio);

                alert.setTitle("Are you sure you want to delete this question and it's related answers: ");
                alert.setMessage(selectedQuestion.getTitle());

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
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

        //Register buttons
        Button submitQMButtonEdit = (Button) root.findViewById(R.id.submitButtonEdit);

        Button addTagButtonEdit = (Button) root.findViewById(R.id.addTagButtonEdit);

        //Allow user to add a tag
        addTagButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext(),
                        R.style.Theme_Mobdevportfolio);

                alert.setTitle("Add tag");
                alert.setMessage("Add a tag");

                // Set an EditText view to get user input
                EditText input = new EditText(getActivity().getApplicationContext());
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (input.getText().length() == 0) {
                            Toast.makeText(getContext(),
                                            "Tag name must not be null", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            db.tagDao().insertAll(new Tag(input.getText().toString()));
                            Navigation.findNavController(v).navigate(R.id.nav_edit_question,
                                    bundleReceived);
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

        //Populate Tag List
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

        //Pre-populate fields with the existing data
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

        //Update question in the database
        submitQMButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllFieldsChecked = CheckAllFields();

                if(isAllFieldsChecked) {

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

                    db.questionDao().updateQuestionCorrectAnswer(
                            Integer.parseInt(binding.spinnerCorrectAnswerEditID.getSelectedItem().toString())
                            , db.questionDao().getQuestionIDByName(
                                    binding.editTextQuestionTitleEdit.getText().toString()
                            ));

                    Navigation.findNavController(v).navigate(R.id.action_nav_edit_question_to_nav_question_manager);
                }
            }
        });

        return root;
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
        }

        if (editTextAnswer1.length() == 0) {
            editTextAnswer1.setError("This field is required to be entered");
            return false;
        }

        if (editTextAnswer2.length() == 0) {
            editTextAnswer2.setError("This field is required to be entered");
            return false;
        }

        if (editTextAnswer3.length() == 0) {
            editTextAnswer3.setError("This field is required to be entered");
            return false;
        }

        if (editTextAnswer4.length() == 0) {
            editTextAnswer4.setError("This field is required to be entered");
            return false;
        }

        // after all validation return true.
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}