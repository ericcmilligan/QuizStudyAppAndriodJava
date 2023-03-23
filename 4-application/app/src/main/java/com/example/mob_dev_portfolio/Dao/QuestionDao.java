package com.example.mob_dev_portfolio.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.mob_dev_portfolio.Entities.Question;

import java.util.List;

@Dao
public interface QuestionDao {
    @Query("SELECT * FROM Question")
    List<Question> getAllQuestions();

    @Query("SELECT * FROM Question WHERE QuestionID = 1")
    Question getQuestionOne();

    @Query("SELECT QuestionID FROM Question WHERE Title = :questionTitle")
    Integer getQuestionIDByName(String questionTitle);

    @Insert
    void insertAll(Question...questions);

    @Delete
    void delete(Question question);

    @Query("UPDATE question SET correctAnswerID = :correctAnswerID WHERE QuestionID = :id")
    public abstract int updateQuestionCorrectAnswer(Integer correctAnswerID, Integer id);
}
