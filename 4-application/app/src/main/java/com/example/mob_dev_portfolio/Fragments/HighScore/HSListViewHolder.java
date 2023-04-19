package com.example.mob_dev_portfolio.Fragments.HighScore;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob_dev_portfolio.R;

/**
 * A custom view-holder class that contains the layout for an individual high-score item
 */
public class HSListViewHolder extends RecyclerView.ViewHolder {

    public TextView highScoreTagTextView;
    public TextView highScoreTextView;
    public TextView highScoreDateTextView;
    public Button resetHighScoreButton;
    public Button shareHighScoreButton;

    public HSListViewHolder(@NonNull View itemView) {
        super(itemView);
        highScoreTagTextView = itemView.findViewById(R.id.highScoreTagTextView);
        highScoreTextView = itemView.findViewById(R.id.highScoreTextView);
        highScoreDateTextView = itemView.findViewById(R.id.highScoreDateTextView);
        resetHighScoreButton = itemView.findViewById(R.id.resetHighScoreButton);
        shareHighScoreButton = itemView.findViewById(R.id.shareHighScoreButton);
    }
}
