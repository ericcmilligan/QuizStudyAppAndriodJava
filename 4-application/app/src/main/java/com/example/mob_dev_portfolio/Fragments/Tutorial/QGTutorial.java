package com.example.mob_dev_portfolio.Fragments.Tutorial;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

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
        //Fragment locked in portrait screen orientation as the tutorial does not display correctly
        //in landscape mode
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQuizGameTutorialBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ImageView quizGameTutorialGIF = binding.quizGameTutorialImageView;

        Toast.makeText(getContext(), "Tutorial displays in vertical orientation only",
                Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "Click the back arrow to go to the previous tutorial for the app",
                Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "Click the front arrow to go the next tutorial for the app",
                Toast.LENGTH_SHORT).show();

        return root;
    }

    public void onDestroyView() {
        super.onDestroyView();
        //Set orientation back to landscape and vertical after leaving this fragment
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }
}