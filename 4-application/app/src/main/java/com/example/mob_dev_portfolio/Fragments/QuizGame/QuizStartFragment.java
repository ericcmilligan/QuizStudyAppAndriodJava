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

import com.example.mob_dev_portfolio.Database.QuizDatabase;
import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.databinding.FragmentQuizStartBinding;

/**
 * A fragment representing the start screen for a quiz game for a given category.
 */
public class QuizStartFragment extends Fragment {

    private FragmentQuizStartBinding binding;

    public QuizStartFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQuizStartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        QuizDatabase db = QuizDatabase.getInstance(getActivity().getApplicationContext());

        TextView tagCategoryText = binding.quizStartCategoryText;
        TextView highScoreText = binding.quizStartHighscoreText;

        //Save the bundle information into variables
        String bundleReceivedString = this.getArguments().toString();
        String bundleInt = bundleReceivedString.replaceAll("\\D+","");
        Integer selectedTagID = Integer.parseInt(bundleInt);

        //Get the tag name from tag id in passed bundle
        String selectedTagName = db.tagDao().getTagNameByID(selectedTagID);
        Integer highScorePoints = db.highScoreDao().getHighScorePointsByTagID(selectedTagID);

        //If the tag id selected is zero then report no category has been received
        if(selectedTagID.equals(0)){
            tagCategoryText.setText("No category received");
        } else {
            tagCategoryText.setText(selectedTagName);
        }

        //If the high-score has not been set then set the high score text to zero
        if(highScorePoints != null){
            highScoreText.setText("High-score: " + String.valueOf(highScorePoints));
        } else {
            highScoreText.setText("High-score: 0");
        }

        //Set up start quiz button
        Button buttonStartQuiz = binding.quizStartButton;
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Put the tag id into a bundle
                Bundle bundle = new Bundle();
                bundle.putInt("tagID", selectedTagID);
                //Go the quiz game screen and pass the tag id
                Navigation.findNavController(v).navigate(R.id.action_nav_quiz_start_to_nav_quiz_game,
                        bundle);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}