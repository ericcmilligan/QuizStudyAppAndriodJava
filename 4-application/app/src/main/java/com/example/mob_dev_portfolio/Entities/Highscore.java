package com.example.mob_dev_portfolio.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(foreignKeys = {@ForeignKey(entity = Tag.class,
        parentColumns = "TagID",
        childColumns = "TagID",
        onDelete = ForeignKey.SET_NULL)
})
public class Highscore {
    @PrimaryKey(autoGenerate = true)
    private Integer HighScoreID;

    @ColumnInfo(name = "TagID")
    private Integer TagID;

    @ColumnInfo(name = "Score")
    private Integer Score;

    @ColumnInfo(name = "DateAchieved")
    private LocalDateTime DateAchieved;

    public Highscore(Integer TagID, Integer Score, LocalDateTime DateAchieved) {
        this.TagID = TagID;
        this.Score = Score;
        this.DateAchieved = DateAchieved;
    }

    public Integer getHighScoreID() {
        return HighScoreID;
    }

    public void setHighScoreID(Integer highScoreID) {
        HighScoreID = highScoreID;
    }

    public Integer getTagID() {
        return TagID;
    }

    public void setTagID(Integer tagID) {
        TagID = tagID;
    }

    public Integer getScore() {
        return Score;
    }

    public void setScore(Integer score) {
        Score = score;
    }

    public LocalDateTime getDateAchieved() {
        return DateAchieved;
    }

    public void setDateAchieved(LocalDateTime dateAchieved) {
        DateAchieved = dateAchieved;
    }

    @Override
    public String toString() {
        return "HighScore{" +
                "HighScoreID=" + HighScoreID +
                ", TagID=" + TagID +
                ", Score=" + Score +
                ", DateAchieved=" + DateAchieved +
                '}';
    }
}
