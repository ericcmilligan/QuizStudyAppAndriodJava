package com.example.mob_dev_portfolio.Fragments.QuizGame;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mob_dev_portfolio.Database.QuizDatabase;
import com.example.mob_dev_portfolio.Entities.Tag;
import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.databinding.FragmentQcListBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of quiz categories(tags).
 */
public class QCListFragment extends Fragment {

    FragmentQcListBinding binding;

    List<String> tagsList = new ArrayList<>();

    ArrayAdapter adapter;

    ListView lv;

    public QCListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentQcListBinding.inflate(inflater, container, false);

        View view = inflater.inflate(R.layout.fragment_qc_list, container, false);

        //Set up button to allow user to go back to homepage
        FloatingActionButton backToHomeButton = (FloatingActionButton) view.findViewById(R.id.backToHomeQCButton);

        backToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_quiz_category_to_nav_home);
            }
        });

        //Set up button for providing help to the user for the current screen
        FloatingActionButton qcHelperButton = (FloatingActionButton) view.findViewById(R.id.qcHelperButton);

        qcHelperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Click a tag within the list" +
                        " to proceed to play a quiz based on the category", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Clear the tag on view resume if already populated
        if(tagsList.size() > 0){
            tagsList.clear();
        }

        //Populate the tag list with all tag names present in the database
        QuizDatabase db = QuizDatabase.getInstance(getActivity().getApplicationContext());

        List<Tag> tagList = db.tagDao().getAllTags();

        for(int i = 0; i < tagList.size(); i++){
            tagsList.add(i, tagList.get(i).getName());
        }

        this.lv = (ListView) view.findViewById(R.id.tag_list_view);

        this.adapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                tagsList
        );

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get number of questions for tags from database
                Integer numOfQuestionsForTag = db.questionDao()
                        .getQuestionsByTagID((tagList.get(position).getTagID()) - 1).size();

                //Go to quiz start screen if number of questions for tags is at least 1
                if(numOfQuestionsForTag.equals(0)){
                    Toast.makeText(getActivity().getApplicationContext(), "Number of questions for tag must not be zero",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Navigation.findNavController(view).navigate(R.id.action_nav_quiz_category_to_nav_quiz_start);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.tagListView.setAdapter(null);
        binding = null;
    }
}