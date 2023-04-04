package com.example.mob_dev_portfolio.Fragments.HighScore;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob_dev_portfolio.R;

public class HSListViewHolder extends RecyclerView.ViewHolder {

    public TextView highScoreTextView;
    public Button resetHighScoreButton;
    public Button shareHighScoreButton;

    public HSListViewHolder(@NonNull View itemView) {
        super(itemView);
        highScoreTextView = itemView.findViewById(R.id.highScoreTextView);
        resetHighScoreButton = itemView.findViewById(R.id.resetHighScoreButton);
        shareHighScoreButton = itemView.findViewById(R.id.shareHighScoreButton);
    }
}
