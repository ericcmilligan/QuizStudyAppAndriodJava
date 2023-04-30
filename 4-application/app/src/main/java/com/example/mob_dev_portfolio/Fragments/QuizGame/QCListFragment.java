package com.example.mob_dev_portfolio.Fragments.QuizGame;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mob_dev_portfolio.Database.QuizDatabase;
import com.example.mob_dev_portfolio.Entities.Tag;
import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.databinding.FragmentQcListBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of quiz categories(tags).
 */
public class QCListFragment extends Fragment {

    FragmentQcListBinding binding;

    List<String> tagsList = new ArrayList<>();

    ArrayAdapter adapter;

    ListView lv;

    //Initialize shared preferences
    SharedPreferences sharedPreferences = null;

    public QCListFragment() {

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

        binding = FragmentQcListBinding.inflate(inflater, container, false);

        View view = inflater.inflate(R.layout.fragment_qc_list, container, false);

        //Set up button to allow user to go back to homepage
        FloatingActionButton backToHomeButton = (FloatingActionButton) view.findViewById(R.id.backToHomeQCButton);

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

                Navigation.findNavController(v).navigate(R.id.action_nav_quiz_category_to_nav_home);
            }
        });

        //Set up button for providing help to the user for the current screen
        FloatingActionButton qcHelperButton = (FloatingActionButton) view.findViewById(R.id.qcHelperButton);

        qcHelperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                showQCHelpPopUp();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Clear the tag on view resume if already populated
        if(tagsList.size() > 0){
            tagsList.clear();
        }

        //Populate the tag list with all tag names present in the database
        QuizDatabase db = QuizDatabase.getInstance(getActivity().getApplicationContext());

        List<Tag> tagList = db.tagDao().getAllTags();

        for(int i = 0; i < tagList.size(); i++){
            tagsList.add(i, tagList.get(i).getName());
        }

        this.lv = (ListView) view.findViewById(R.id.tag_list_view);

        this.adapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                tagsList
        );

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaPlayer buttonClickSound = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                //Get number of questions for tags from database
                Integer tagID = tagList.get(position).getTagID();
                Integer numOfQuestionsForTag = db.questionDao()
                        .getQuestionsByTagID(tagID).size();

                //Go to quiz start screen if number of questions for tags is at least 1
                if(numOfQuestionsForTag.equals(0)){
                    Toast.makeText(getActivity().getApplicationContext(), "Number of questions for tag must not be zero",
                            Toast.LENGTH_SHORT).show();
                } else {
                    //Put the tag id into a bundle
                    Bundle bundle = new Bundle();
                    bundle.putInt("tagID", tagID);
                    //Go the quiz start screen and pass the tag id
                    Navigation.findNavController(view).navigate(R.id.action_nav_quiz_category_to_nav_quiz_start,
                            bundle);
                }
            }
        });
    }

    private void showQCHelpPopUp() {
        //Show the user a pop-up with information on the quiz game
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext(),
                androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);

        alert.setTitle("Quiz Game Help");
        alert.setMessage(
                "1.You can click a tag within the list to proceed to play a quiz based on the " +
                        "tag(category)."
        );

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Show quiz category help pop up if it is the first time visiting the fragment after install
        if(sharedPreferences.getBoolean("first-run-qc-help", true)) {
            showQCHelpPopUp();
            //Set first run to false after this method has finished
            sharedPreferences.edit().putBoolean("first-run-qc-help", false).commit();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.tagListView.setAdapter(null);
        binding = null;
    }
}