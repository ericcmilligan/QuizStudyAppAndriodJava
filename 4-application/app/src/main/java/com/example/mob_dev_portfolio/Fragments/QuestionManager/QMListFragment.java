package com.example.mob_dev_portfolio.Fragments.QuestionManager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class QMListFragment extends Fragment {


    FragmentQmListBinding binding;

    List<String> questionTitleList = new ArrayList<>();

    List<Question> questionsList = new ArrayList<>();

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

        FloatingActionButton addQuestionButton = (FloatingActionButton) view.findViewById(R.id.addQuestionButton);

        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_question_manager_to_nav_add_question);
            }
        });

        FloatingActionButton backToHomeButton = (FloatingActionButton) view.findViewById(R.id.backToHomeButton);

        backToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_question_manager_to_nav_home);
            }
        });

        FloatingActionButton qmHelperButton = (FloatingActionButton) view.findViewById(R.id.qmHelperButton);

        qmHelperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Click the plus icon to add a new question"
                        , Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity().getApplicationContext(), "Scroll the list to view questions in the system and click a question to edit"
                        , Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        QuizDatabase db = QuizDatabase.getInstance(getActivity().getApplicationContext());

        questionsList = db.questionDao().getAllQuestions();

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

                Bundle bundle = new Bundle();
                bundle.putInt("questionID", questionID);

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

        //Change list selection on drop-down change
        spinnerTagChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerTagChooser.getSelectedItem().toString().equals("All")){
                    questionTitleList.clear();

                    questionsList = db.questionDao().getAllQuestions();

                    for (int i = 0; i < questionsList.size(); i++) {
                        questionTitleList.add(i, questionsList.get(i).getTitle());
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    questionTitleList.clear();

                    Integer selectedTagID = db.tagDao().getTagIDByName(spinnerTagChooser.getSelectedItem().toString());

                    questionsList = db.questionDao().getQuestionsByTagID(selectedTagID);

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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}