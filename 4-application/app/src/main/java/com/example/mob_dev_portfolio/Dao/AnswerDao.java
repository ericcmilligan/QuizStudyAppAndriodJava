package com.example.mob_dev_portfolio.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.mob_dev_portfolio.Entities.Answer;

import java.util.List;

@Dao
public interface AnswerDao {
    @Query("SELECT * FROM Answer")
    List<Answer> getAllAnswers();

    @Query("SELECT * FROM Answer WHERE AnswerID = 1")
    Answer getAnswerOne();

    @Insert
    void insertAll(Answer...answers);

    @Delete
    void delete(Answer answer);
}
