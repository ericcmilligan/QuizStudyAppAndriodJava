package com.example.mob_dev_portfolio.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.mob_dev_portfolio.Entities.Answer;

import java.util.List;

/**
 * A DAO class for the answer table in the database.
 */
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

    @Query("DELETE FROM Answer WHERE QuestionID = :questionID")
    void deleteAnswersByQuestionID(Integer questionID);

    @Query("UPDATE answer SET QuestionID = :questionID, Text = :text WHERE AnswerID = :answerID")
    void updateAnswer(Integer answerID, Integer questionID, String text);
}
