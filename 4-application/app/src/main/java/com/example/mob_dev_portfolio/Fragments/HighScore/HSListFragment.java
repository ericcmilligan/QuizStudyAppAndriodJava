package com.example.mob_dev_portfolio.Fragments.HighScore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.databinding.FragmentHighScoreBinding;
import com.example.mob_dev_portfolio.databinding.FragmentHsListBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of quiz high scores
 */
public class HSListFragment extends Fragment {

    FragmentHsListBinding binding;

    List<String> highScoresList = new ArrayList<>();

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

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}