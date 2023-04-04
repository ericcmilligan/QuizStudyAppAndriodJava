package com.example.mob_dev_portfolio.Fragments.HighScore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mob_dev_portfolio.Entities.Highscore;
import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.databinding.FragmentHsListBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * A fragment representing a list of quiz high scores
 */
public class HSListFragment extends Fragment {

    FragmentHsListBinding binding;
    ArrayList<Highscore> highScoresList = new ArrayList<Highscore>();
    HSListAdapter hsListAdapter;
    RecyclerView recyclerView;

    public HSListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHsListBinding.inflate(inflater, container, false);

        View view = inflater.inflate(R.layout.fragment_hs_list, container, false);

        //Set up button to go back to homepage
        FloatingActionButton backToHomeButton = (FloatingActionButton) view.findViewById(R.id.backToHomeHSButton);

        backToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_quiz_high_score_to_nav_home);
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);


        recyclerView = view.findViewById(R.id.high_score_list_view);

        recyclerView.setLayoutManager(llm);

        hsListAdapter = new HSListAdapter(getContext(), highScoresList);

        recyclerView.setAdapter(hsListAdapter);

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        highScoresList.add(new Highscore(1, 1, LocalDateTime.now()));
        highScoresList.add(new Highscore(2, 2, LocalDateTime.now()));

        hsListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}