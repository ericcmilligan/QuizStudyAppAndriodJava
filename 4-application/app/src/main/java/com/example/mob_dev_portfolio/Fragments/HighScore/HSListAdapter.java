package com.example.mob_dev_portfolio.Fragments.HighScore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mob_dev_portfolio.Database.QuizDatabase;
import com.example.mob_dev_portfolio.Entities.Highscore;
import com.example.mob_dev_portfolio.R;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HSListAdapter extends RecyclerView.Adapter<HSListViewHolder> {

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
        Highscore highscore = highScoreList.get(position);

        QuizDatabase db = QuizDatabase.getInstance(context);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        holder.highScoreTagTextView.setText("Tag Name: " + db.tagDao().getTagNameByID(highscore.getTagID()));
        holder.highScoreTextView.setText("High-Score: " + highscore.getScore());
        holder.highScoreDateTextView.setText("Date Achieved: " + highscore.getDateAchieved().format(formatter));

        holder.resetHighScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuizDatabase db = QuizDatabase.getInstance(context);
                db.highScoreDao().resetHighScoreByID(highscore.getHighScoreID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return highScoreList.size();
    }
}
