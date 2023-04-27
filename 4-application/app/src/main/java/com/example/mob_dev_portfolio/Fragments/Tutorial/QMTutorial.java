package com.example.mob_dev_portfolio.Fragments.Tutorial;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.databinding.FragmentQuestionManagerTutorialBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

        //Use glide image library to display the question manager tutorial GIF
        ImageView questionMangerTutorialGif = binding.questionManagerTutorialImageView;

        Glide.with(getContext())
                .asGif()
                .load(R.drawable.qmtutorial)
                .placeholder(R.drawable.placeholder)
                .into(questionMangerTutorialGif);

        //Tell the user that the tutorial only works in vertical orientation due to the video's
        //aspect ratio
        Toast.makeText(getContext(), "Tutorial displays in vertical orientation only",
                Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "Click the next arrow to go to the next tutorial for the app",
                Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "Click the home button to return home",
                Toast.LENGTH_SHORT).show();

        //Set up button for going back to the home screen from the tutorial
        FloatingActionButton qmTutHomeButton = binding.quitQuestionManagerTutorialButton;
        qmTutHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                Navigation.findNavController(v).navigate(R.id.action_nav_question_manager_tutorial_to_nav_home);
            }
        });

        //Set up button for going to the next tutorial screen
        FloatingActionButton qmTutNextTutButton = binding.questionManagerNextTutorialButton;
        qmTutNextTutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                Navigation.findNavController(v).navigate(R.id.action_nav_question_manager_tutorial_to_nav_quiz_game_tutorial);
            }
        });


        return root;
    }

    public void onDestroyView() {
        super.onDestroyView();
        //Set orientation back to landscape and vertical after leaving this fragment
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }
}