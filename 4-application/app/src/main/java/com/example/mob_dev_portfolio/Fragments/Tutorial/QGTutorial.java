package com.example.mob_dev_portfolio.Fragments.Tutorial;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mob_dev_portfolio.databinding.FragmentQuizGameTutorialBinding;

/**
 * This fragment contains the quiz game tutorial to guide the user on how playing a quiz
 * in this quiz app works.
 */
public class QGTutorial extends Fragment {

    private FragmentQuizGameTutorialBinding binding;

    public QGTutorial() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQuizGameTutorialBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }
}