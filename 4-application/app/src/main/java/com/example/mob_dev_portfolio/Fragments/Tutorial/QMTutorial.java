package com.example.mob_dev_portfolio.Fragments.Tutorial;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.databinding.FragmentQuestionManagerTutorialBinding;

/**
 * This fragment contains the question manager tutorial to guide the user on how managing questions
 * in this quiz app works.
 */
public class QMTutorial extends Fragment {

    private FragmentQuestionManagerTutorialBinding binding;

    public QMTutorial() {

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
        binding = FragmentQuestionManagerTutorialBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ImageView questionMangerTutorialGif = binding.questionManagerTutorialImageView;

        Glide.with(getContext())
                .asGif()
                .load(R.drawable.qmtutorial)
                .placeholder(R.drawable.placeholder)
                .into(questionMangerTutorialGif);

        Toast.makeText(getContext(), "Tutorial displays in vertical orientation only",
                Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "Click the next arrow to go to the next tutorial for the app",
                Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "Click the home button to return home",
                Toast.LENGTH_SHORT).show();

        return root;
    }

    public void onDestroyView() {
        super.onDestroyView();
        //Set orientation back to landscape and vertical after leaving this fragment
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }
}