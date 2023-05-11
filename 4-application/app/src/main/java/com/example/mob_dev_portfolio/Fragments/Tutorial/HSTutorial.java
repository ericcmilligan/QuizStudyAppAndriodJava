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
import com.example.mob_dev_portfolio.databinding.FragmentHighScoreTutorialBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * This fragment contains the high score tutorial to guide the user on how high scores work in
 * in this quiz app.
 */
public class HSTutorial extends Fragment {

    //Set up fragment binding variable
    private FragmentHighScoreTutorialBinding binding;

    public HSTutorial() {

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
        binding =  FragmentHighScoreTutorialBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Use glide image library to display the high score tutorial GIF
        ImageView highScoreTutorialGIF = binding.highScoreTutorialImageView;

        Glide.with(getContext())
                .asGif()
                .load(R.drawable.hstutorial)
                .placeholder(R.drawable.placeholder)
                .into(highScoreTutorialGIF);

        //Tell the user that the tutorial only works in vertical orientation due to the video's
        //aspect ratio 
        Toast.makeText(getContext(), "Tutorial displays in vertical orientation only",
                Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "Click the back arrow to go to the previous tutorial for the app",
                Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "Click the home button to return home",
                Toast.LENGTH_SHORT).show();

        //Set up button for going to the previous tutorial screen
        FloatingActionButton hsTutPreviousTutButton = binding.highScorePreviousTutorialButton;
        hsTutPreviousTutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                Navigation.findNavController(v).navigate(R.id.action_nav_high_score_tutorial_to_nav_quiz_game_tutorial);
            }
        });

        //Set up button for going back to the home screen from the tutorial
        FloatingActionButton hsTutHomeButton= binding.highScoreTutorialBackHomeButton;
        hsTutHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                Navigation.findNavController(v).navigate(R.id.action_nav_high_score_tutorial_to_nav_home);
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