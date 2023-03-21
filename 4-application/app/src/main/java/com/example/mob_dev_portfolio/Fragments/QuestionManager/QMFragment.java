package com.example.mob_dev_portfolio.Fragments.QuestionManager;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.databinding.FragmentQuestionManagerBinding;

/**
 * A fragment representing a list of Items.
 */
public class QMFragment extends Fragment {


    private FragmentQuestionManagerBinding binding;

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;

    public QMFragment() {
    }

    public static QMFragment newInstance(int columnCount) {
        QMFragment fragment = new QMFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQuestionManagerBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_question_manager_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new QMRVAdapter(QMContent.ITEMS));
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}