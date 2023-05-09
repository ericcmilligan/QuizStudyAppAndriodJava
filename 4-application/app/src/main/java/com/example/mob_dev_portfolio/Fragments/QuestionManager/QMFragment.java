package com.example.mob_dev_portfolio.Fragments.QuestionManager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mob_dev_portfolio.R;

/**
 * A fragment to hold the question list fragment.
 */
public class QMFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //This fragment contains the list fragment for question management
        return inflater.inflate(R.layout.fragment_qm, container, false);
    }
}