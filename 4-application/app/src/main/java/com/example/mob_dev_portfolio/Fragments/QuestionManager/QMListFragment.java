package com.example.mob_dev_portfolio.Fragments.QuestionManager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.mob_dev_portfolio.Database.QuizDatabase;
import com.example.mob_dev_portfolio.Entities.Question;
import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.databinding.FragmentQmListBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class QMListFragment extends Fragment {


    FragmentQmListBinding binding;

    List<String> questionTitleList = new ArrayList<>();

    List<Question> questionsList = new ArrayList<>();

    ListAdapter adapter;

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

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        QuizDatabase db = QuizDatabase.getInstance(getActivity().getApplicationContext());

        questionsList = db.questionDao().getAllQuestions();

        for(int i = 0; i < questionsList.size(); i++){
            questionTitleList.add(i, questionsList.get(i).getTitle());
        }

        this.lv = (ListView) view.findViewById(R.id.question_list_view);

        this.adapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                questionTitleList);

        lv.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}