package com.example.mob_dev_portfolio.Fragments.Tutorial;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

        return root;
    }
}