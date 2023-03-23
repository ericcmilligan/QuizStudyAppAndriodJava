package com.example.mob_dev_portfolio.Fragments.QuestionManager;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toolbar;

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

        //Submit question into the database
        submitQMButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        FloatingActionButton backToQMButton = (FloatingActionButton) root.findViewById(R.id.backToQMButton);

        backToQMButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_add_question_to_nav_question_manager);
            }
        });

        return root;
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

}