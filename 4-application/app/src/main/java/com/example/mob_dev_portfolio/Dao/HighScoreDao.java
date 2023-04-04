package com.example.mob_dev_portfolio.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mob_dev_portfolio.Entities.Highscore;

import java.time.LocalDateTime;
import java.util.List;

@Dao
public interface HighScoreDao {
    @Query("SELECT * FROM Highscore")
    List<Highscore> getAllHighScores();

    @Query("SELECT * FROM Highscore WHERE HighScoreID = 1")
    Highscore getHighScoreOne();

    @Query("SELECT Score FROM Highscore WHERE TagID = :tagID")
    Integer getHighScorePointsByTagID(Integer tagID);

    @Query("UPDATE Highscore SET Score = :score, DateAchieved = :now WHERE TagID = :tagID")
    void updateHighScoreByTagID(Integer tagID, Integer score, LocalDateTime now);

    @Insert
    void insertAll(Highscore...highScores);

    @Delete
    void delete(Highscore highScore);

    @Query("DELETE FROM Highscore WHERE TagID = :tagID")
    void deleteHighScoreByTagID(Integer tagID);
}
