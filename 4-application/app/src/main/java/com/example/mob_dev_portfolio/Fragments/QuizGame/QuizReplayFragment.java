package com.example.mob_dev_portfolio.Fragments.QuizGame;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.mob_dev_portfolio.API.DataModel;
import com.example.mob_dev_portfolio.Database.QuizDatabase;
import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.databinding.FragmentQuizReplayBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A fragment to display after the quiz game has finished that shows the user the high-score and
 * asks if they would like to quit or replay the quiz with buttons to do this.
 */
public class QuizReplayFragment extends Fragment {

    private FragmentQuizReplayBinding binding;

    //Variables for Giphy API
    ArrayList<DataModel> dataModelArrayList = new ArrayList<>();
    public static final String API_KEY = "UGeUJXTeOyZZXtzZXFFD87f9HIMX2bOJ";
    public static final String BASE_URL = "https://api.giphy.com/v1/gifs/search?api_key=";
    public static final String BEFORE_SEARCH_URL = "&q=";
    public static String SEARCH_TERM = "winning";
    public static final String AFTER_SEARCH_URL = "&limit=20&offset=0&rating=g&lang=en";

    //Initialize shared preferences
    SharedPreferences sharedPreferences = null;

    public QuizReplayFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Load project shared preferences file into the shared preferences variable
        sharedPreferences = getContext().getSharedPreferences("com.example.mob_dev_portfolio",
                Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Access the binding, view, bundle and quiz database within this fragment
        binding = FragmentQuizReplayBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Get the required data from the bundle
        Bundle bundle = this.getArguments();
        String bundleQuizScoreString = bundle.get("score").toString();
        String bundleScoreInt = bundleQuizScoreString.replaceAll("\\D+","");
        Integer quizScore = Integer.parseInt(bundleScoreInt);

        String bundleReceivedTagIDString = bundle.get("tagID").toString();
        String bundleTagIDInt = bundleReceivedTagIDString.replaceAll("\\D+","");
        Integer tagID = Integer.parseInt(bundleTagIDInt);

        //Get high-score in order to load correct gif type
        QuizDatabase db = QuizDatabase.getInstance(getActivity().getApplicationContext());
        Integer quizHighScoreForTag = db.highScoreDao().getHighScorePointsByTagID(tagID);

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

                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                //Go back to the quiz category screen
                Navigation.findNavController(root).navigate(R.id.action_nav_quiz_replay_to_nav_quiz_category);
            }
        });

        Button replayQuiz = root.findViewById(R.id.replay_quiz_button);

        //When the replayQuiz button is clicked
        replayQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                //Go to the quiz start screen and pass the tag ID within a bundle
                Bundle bundle = new Bundle();
                bundle.putInt("tagID", tagID);
                Navigation.findNavController(root).navigate(R.id.action_nav_quiz_replay_to_nav_quiz_start,
                        bundle);
            }
        });

        FloatingActionButton helpButton = root.findViewById(R.id.quizReplayHelperButton);

        //When the helper button is clicked
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                showQRHelpPopUp();
            }
        });

        //Set up request queue for Giphy API
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        //Set gif retrieved from API depending on winning/losing condition state
        if(quizScore > quizHighScoreForTag | quizScore.equals(quizHighScoreForTag) & quizScore > 0){
            getGif(requestQueue, "win");
        } else {
            getGif(requestQueue, "lose");
        }

        //Go back to quiz start screen on back press
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Bundle bundle = new Bundle();
                bundle.putInt("tagID", tagID);
                Navigation.findNavController(root).navigate(R.id.action_nav_quiz_replay_to_nav_quiz_start,
                        bundle);
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return root;
    }

    private void showQRHelpPopUp() {
        //Show the user a pop-up with information on the quiz replay screen
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext(),
                androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);

        alert.setTitle("Quiz Replay Help");
        alert.setMessage(
                        "1.You can click the replay quiz button to take the quiz again."
                        +
                        "\n\n" +
                        "2.You can click the quit quiz button to quit the quiz."
        );

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    //Getting the data from the API to display a gif for either winning or losing
    public void getGif(RequestQueue requestQueue, String searchTerm){
        //Get imageview to pass
        ImageView quizResultImage = binding.quizReplayImageView;
        SEARCH_TERM = searchTerm;
        //Construct the API url
        String url = BASE_URL + API_KEY + BEFORE_SEARCH_URL + SEARCH_TERM  + AFTER_SEARCH_URL;
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray dataArray = response.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject obj = dataArray.getJSONObject(i);
                        JSONObject obj1 = obj.getJSONObject("images");
                        JSONObject obj2 = obj1.getJSONObject("downsized_medium");

                        String sourceUrl = obj2.getString("url");

                        dataModelArrayList.add(new DataModel(sourceUrl));
                    }

                    //Shuffle the data model array to get a random gif from giphy response
                    Collections.shuffle(dataModelArrayList);

                    Glide.with(getContext())
                            .asGif()
                            .placeholder(R.drawable.placeholder)
                            .load(dataModelArrayList.get(1).getImageUrl())
                            .into(quizResultImage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Show local gifs for winning or losing instead if the API fails
                if(Objects.equals(searchTerm, "win")){
                    Glide.with(getContext())
                            .asGif()
                            .load(R.drawable.winning)
                            .placeholder(R.drawable.winning)
                            .into(quizResultImage);
                } else if(Objects.equals(searchTerm, "lose")){
                    Glide.with(getContext())
                            .asGif()
                            .placeholder(R.drawable.losing)
                            .load(R.drawable.losing)
                            .into(quizResultImage);
                }
            }
        });

        requestQueue.add(objectRequest);
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
        //Show quiz replay help pop up if it is the first time visiting the fragment after install
        if(sharedPreferences.getBoolean("first-run-qr-help", true)) {
            showQRHelpPopUp();
            //Set first run to false after this method has finished
            sharedPreferences.edit().putBoolean("first-run-qr-help", false).commit();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}