package com.example.mob_dev_portfolio.Fragments.QuestionManager;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.databinding.FragmentAddQuestionBinding;
import com.example.mob_dev_portfolio.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class AddQMFrag extends Fragment {

    private FragmentAddQuestionBinding binding;

    public AddQMFrag() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddQuestionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FloatingActionButton backToQMButton = (FloatingActionButton) root.findViewById(R.id.backToQMButton);

        backToQMButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_add_question_to_nav_question_manager);
            }
        });

        return root;
    }

}