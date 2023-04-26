package com.example.mob_dev_portfolio.Fragments.HighScore;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mob_dev_portfolio.Database.QuizDatabase;
import com.example.mob_dev_portfolio.Entities.Highscore;
import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.databinding.FragmentHsListBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of quiz high scores
 */
public class HSListFragment extends Fragment {

    FragmentHsListBinding binding;
    ArrayList<Highscore> highScoresList = new ArrayList<Highscore>();
    HSListAdapter hsListAdapter;
    RecyclerView recyclerView;

    public HSListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHsListBinding.inflate(inflater, container, false);

        View view = inflater.inflate(R.layout.fragment_hs_list, container, false);

        //Set up button to go back to homepage
        FloatingActionButton backToHomeButton = (FloatingActionButton) view.findViewById(R.id.backToHomeHSButton);

        backToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                Navigation.findNavController(v).navigate(R.id.action_nav_quiz_high_score_to_nav_home);
            }
        });

        //Set up helper button for high score screen
        FloatingActionButton highScoreHelperButton = (FloatingActionButton) view.findViewById(R.id.hsHelperButton);
        highScoreHelperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                //Show the user a pop-up with information on the high-score page
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext(),
                        androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);

                alert.setTitle("High-Score Page Help");
                alert.setMessage(
                                "1.You can scroll the list to see high-scores for a tag." +
                                "\n\n" +
                                "2.You can reset/share a high-score by clicking the respective button."
                );

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        });

        //Set up layout manager and custom adapter for high-score list
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        hsListAdapter = new HSListAdapter(getContext(), highScoresList);

        recyclerView = view.findViewById(R.id.high_score_list_view);

        recyclerView.setLayoutManager(llm);

        recyclerView.setAdapter(hsListAdapter);

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        QuizDatabase db = QuizDatabase.getInstance(getContext());

        List<Highscore> highScores = db.highScoreDao().getAllHighScores();

        //If no high-scores have been created yet inform the user of this
        if(highScores.size() == 0){
            Toast.makeText(getContext(), "Achieve a high-score first to see it here!",
                    Toast.LENGTH_LONG).show();
        } else {
            //Else add to the high-score list each high-score record in the database
            for(Highscore highscore : highScores){
                if(highscore.getTagID() != null){
                    highScoresList.add(highscore);
                }
            }
            hsListAdapter.notifyDataSetChanged();
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}