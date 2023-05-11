package com.example.mob_dev_portfolio.Fragments.HighScore;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mob_dev_portfolio.Database.QuizDatabase;
import com.example.mob_dev_portfolio.Entities.Highscore;
import com.example.mob_dev_portfolio.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * A custom adapter class for displaying high scores
 */
public class HSListAdapter extends RecyclerView.Adapter<HSListViewHolder> {

    //Set up context and high score array list variables
    private Context context;
    private ArrayList<Highscore> highScoreList;

    public HSListAdapter(Context context, ArrayList<Highscore> highScoresList){
        this.context = context;
        this.highScoreList = highScoresList;
    }

    @NonNull
    @Override
    public HSListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.highscore_list_item, parent, false);
        return new HSListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HSListViewHolder holder, int position) {
        //Get the current high-score item from the list
        Highscore highscore = highScoreList.get(position);

        //Initialize the database and date time formatter variables
        QuizDatabase db = QuizDatabase.getInstance(context);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        //Set the high-score list item information
        holder.highScoreTagTextView.setText("Tag Name: " + db.tagDao().getTagNameByID(highscore.getTagID()));
        holder.highScoreTextView.setText("High-Score: " + highscore.getScore());
        holder.highScoreDateTextView.setText("Date Achieved: " + highscore.getDateAchieved().format(formatter));

        //Reset the high-score item's score to 0 on click
        holder.resetHighScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer buttonClickSound = MediaPlayer.create(context.getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                QuizDatabase db = QuizDatabase.getInstance(context);

                String tagName = db.tagDao().getTagNameByID(highscore.getTagID());

                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext(),
                        androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);

                alert.setTitle("Are you sure you want to reset the high-score for this tag?");
                alert.setMessage(tagName);

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Reset the high-score if not null
                        if(highscore.getScore() != null){
                            db.highScoreDao().resetHighScoreByID(highscore.getHighScoreID());
                            Navigation.findNavController(v).navigate(R.id.nav_high_score);
                        } else {
                            Toast.makeText(v.getContext(), "Please make sure the high-score" +
                                    "points is not null", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled
                    }
                });

                alert.show();
            }
        });

        //Share high-score via external email app
        holder.shareHighScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer buttonClickSound = MediaPlayer.create(context.getApplicationContext(), R.raw.click);

                if(buttonClickSound != null){
                    buttonClickSound.start();
                    if(!buttonClickSound.isPlaying()){
                        buttonClickSound.release();
                    }
                }

                //Set up variables to share in the email
                QuizDatabase db = QuizDatabase.getInstance(context);
                String tagName = db.tagDao().getTagNameByID(highscore.getTagID());
                String emailSubject = "High-Score Achieved For: " + tagName;
                String emailText = "I achieved a high-score of " + highscore.getScore() + " points for the quiz category "
                        + tagName + "!";

                //Create email intent
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, emailText);

                //Open email intent
                emailIntent.setType("message/rfc822");
                v.getContext().startActivity(Intent.createChooser(emailIntent, "Choose an email client"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return highScoreList.size();
    }
}
