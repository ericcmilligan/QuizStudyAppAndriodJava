package com.example.mob_dev_portfolio.Fragments.QuizGame;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mob_dev_portfolio.R;


/**
 * A fragment to contain the list of quiz categories(tags).
 */
public class QuizCategoryFragment extends Fragment {

    public QuizCategoryFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz_category, container, false);
    }

}