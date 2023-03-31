package com.example.mob_dev_portfolio.Fragments.QuizGame;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mob_dev_portfolio.Database.QuizDatabase;
import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.databinding.FragmentQuizReplayBinding;

/**
 * A fragment to display after the quiz game has finished that shows the user the high-score and
 * asks if they would like to quit or replay the quiz with buttons to do this.
 */
public class QuizReplayFragment extends Fragment {

    private FragmentQuizReplayBinding binding;

    public QuizReplayFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Access the binding, view, bundle and quiz database within this fragment
        binding = FragmentQuizReplayBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String bundleReceivedString = this.getArguments().toString();
        String bundleInt = bundleReceivedString.replaceAll("\\D+","");
        Integer quizScore = Integer.parseInt(bundleInt);

        //Show the user the result of the quiz
        if(quizScore == null){
                Toast.makeText(getContext(), "No score found for this quiz",
                        Toast.LENGTH_LONG).show();
        } else {
                //Set the score textview to display the score result of the quiz
                TextView scoreAchievedText = root.findViewById(R.id.quiz_replay_score_text);
                scoreAchievedText.setText("Score Achieved: " + String.valueOf(quizScore));
        }

        Button quitQuiz = root.findViewById(R.id.quit_quiz_button);

        //When the quit quiz button is clicked
        quitQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go back to the quiz category screen
                Navigation.findNavController(root).navigate(R.id.action_nav_quiz_replay_to_nav_quiz_category);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    //Hide the toolbar for the quiz replay screen
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}