package com.example.mob_dev_portfolio.Fragments.QuizGame;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
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
import com.example.mob_dev_portfolio.Entities.Highscore;
import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.databinding.FragmentQuizReplayBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

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

        //Get the required data from the bundle
        Bundle bundle = this.getArguments();
        String bundleQuizScoreString = bundle.get("score").toString();
        String bundleScoreInt = bundleQuizScoreString.replaceAll("\\D+","");
        Integer quizScore = Integer.parseInt(bundleScoreInt);

        String bundleReceivedTagIDString = bundle.get("tagID").toString();
        String bundleTagIDInt = bundleReceivedTagIDString.replaceAll("\\D+","");
        Integer tagID = Integer.parseInt(bundleTagIDInt);

        //Get high-score to load correct gif type
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
                //Go back to the quiz category screen
                Navigation.findNavController(root).navigate(R.id.action_nav_quiz_replay_to_nav_quiz_category);
            }
        });

        Button replayQuiz = root.findViewById(R.id.replay_quiz_button);

        //When the replayQuiz button is clicked
        replayQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                //Help the user
                Toast.makeText(getContext(), "Click the replay quiz button to take the quiz again",
                        Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "Click the quit quiz button to quit the quiz",
                        Toast.LENGTH_SHORT).show();
            }
        });

        //Set up request queue for Giphy API
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        //Set gif retrieved from API depending on winning/losing condition state
        if(quizScore > quizHighScoreForTag | quizScore.equals(quizHighScoreForTag)){
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

    public void getGif(RequestQueue requestQueue, String searchTerm){
        //Getting the data from the API to display a gif for either winning or losing
        SEARCH_TERM = searchTerm;
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

                    //Get random gif image url for the array
                    Collections.shuffle(dataModelArrayList);

                    //Load gif into the image view with glide
                    ImageView quizResultImage = getActivity().findViewById(R.id.quiz_replay_image_view);

                    Glide.with(getContext())
                            .asGif()
                            .load(dataModelArrayList.get(1).getImageUrl())
                            .into(quizResultImage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error occurred while getting gif, not loaded" +
                                error.getMessage(),
                        Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}