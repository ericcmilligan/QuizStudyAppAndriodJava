package com.example.mob_dev_portfolio.Fragments.Tutorial;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mob_dev_portfolio.databinding.FragmentHighScoreTutorialBinding;

/**
 * This fragment contains the high score tutorial to guide the user on how high scores work in
 * in this quiz app.
 */
public class HSTutorial extends Fragment {

    private FragmentHighScoreTutorialBinding binding;

    public HSTutorial() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =  FragmentHighScoreTutorialBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }
}