package com.example.mob_dev_portfolio.Fragments.QuestionManager;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.example.mob_dev_portfolio.databinding.FragmentAddQuestionBinding;
import com.example.mob_dev_portfolio.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class AddQMFrag extends Fragment {

    private FragmentAddQuestionBinding binding;
    boolean isAllFieldsChecked = false;

    public AddQMFrag() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddQuestionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        QuizDatabase db = QuizDatabase.getInstance(getActivity().getApplicationContext());

        Button submitQMButton = (Button) root.findViewById(R.id.submitButton);

        Button addTagButton = (Button) root.findViewById(R.id.addTagButton);

        //Allow user to add a tag
        addTagButton.setOnClickListener(new View.OnClickListener() {
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
                                    Navigation.findNavController(v).navigate(R.id.nav_add_question);
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

        //Submit question into the database
        submitQMButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllFieldsChecked = CheckAllFields();

                if(isAllFieldsChecked) {
                    db.questionDao().insertAll(
                            new Question(
                                    db.tagDao().getTagIDByName(
                                            binding.spinnerTagID.getSelectedItem().toString()
                                    ),
                                    null,
                                    binding.editTextQuestionTitle.getText().toString())
                    );

                    db.answerDao().insertAll(
                            new Answer(
                                    db.questionDao().getQuestionIDByName(
                                            binding.editTextQuestionTitle.getText().toString()
                                    ),
                                    binding.editTextAnswer1.getText().toString()),
                            new Answer(db.questionDao().getQuestionIDByName(
                                    binding.editTextQuestionTitle.getText().toString()
                            ),
                                    binding.editTextAnswer2.getText().toString()),
                            new Answer(db.questionDao().getQuestionIDByName(
                                    binding.editTextQuestionTitle.getText().toString()
                            ),
                                    binding.editTextAnswer3.getText().toString()),
                            new Answer(db.questionDao().getQuestionIDByName(
                                    binding.editTextQuestionTitle.getText().toString()
                            ),
                                    binding.editTextAnswer4.getText().toString())
                    );

                    db.questionDao().updateQuestionCorrectAnswer(
                            Integer.parseInt(binding.spinnerCorrectAnswerID.getSelectedItem().toString())
                            , db.questionDao().getQuestionIDByName(
                                    binding.editTextQuestionTitle.getText().toString()
                            ));

                    Navigation.findNavController(v).navigate(R.id.action_nav_add_question_to_nav_question_manager);
                }
            }
        });

        FloatingActionButton backToQMButton = (FloatingActionButton) root.findViewById(R.id.backToQMButton);

        backToQMButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_add_question_to_nav_question_manager);
            }
        });

        FloatingActionButton addQuestionHelperButton  = (FloatingActionButton) root.findViewById(R.id.addQuestionHelperButton);

        addQuestionHelperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Fill out the form and scroll down to click submit"
                        , Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity().getApplicationContext(), "You can add a new tag by pressing the button"
                        , Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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