package com.example.mob_dev_portfolio.Fragments.QuestionManager;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
import com.example.mob_dev_portfolio.Entities.Question;
import com.example.mob_dev_portfolio.Entities.Tag;
import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.databinding.FragmentQmListBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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


    public QMListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                Navigation.findNavController(v).navigate(R.id.action_nav_question_manager_to_nav_add_question);
            }
        });

        //Set up button to allow user to go back to the home-screen
        FloatingActionButton backToHomeButton = (FloatingActionButton) view.findViewById(R.id.backToHomeButton);

        backToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_question_manager_to_nav_home);
            }
        });

        //Set up button for providing help to the user for the current screen
        FloatingActionButton qmHelperButton = (FloatingActionButton) view.findViewById(R.id.qmHelperButton);

        qmHelperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Change the tag through the drop-down list to view questions for a given tag"
                        , Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity().getApplicationContext(), "Scroll the list to view questions for the chosen tag and click a question to edit"
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
                            R.style.Theme_Mobdevportfolio);

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
                            R.style.Theme_Mobdevportfolio);

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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}