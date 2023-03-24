package com.example.mob_dev_portfolio.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.example.mob_dev_portfolio.Entities.Answer;
import com.example.mob_dev_portfolio.Entities.Question;

import java.util.List;

@Dao
public interface AnswerDao {
    @Query("SELECT * FROM Answer")
    List<Answer> getAllAnswers();

    @Query("SELECT * FROM Answer WHERE QuestionID = :questionID")
    List<Answer> getAllAnswersForQuestion(Integer questionID);

    @Query("SELECT * FROM Answer WHERE AnswerID = 1")
    Answer getAnswerOne();

    @Insert
    void insertAll(Answer...answers);

    @Delete
    void delete(Answer answer);

    @Query("UPDATE answer SET QuestionID = :questionID, Text = :text WHERE AnswerID = :answerID")
    void updateAnswer(Integer answerID, Integer questionID, String text);
}
