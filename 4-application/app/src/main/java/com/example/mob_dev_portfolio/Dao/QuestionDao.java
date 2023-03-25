package com.example.mob_dev_portfolio.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.example.mob_dev_portfolio.Entities.Question;

import java.util.List;

@Dao
public interface QuestionDao {
    @Query("SELECT * FROM Question")
    List<Question> getAllQuestions();

    @Query("SELECT * FROM Question WHERE QuestionID = :questionID")
    Question getQuestionByID(Integer questionID);

    @Query("SELECT QuestionID FROM Question WHERE Title = :questionTitle")
    Integer getQuestionIDByName(String questionTitle);

    @Query("SELECT CorrectAnswerID FROM Question WHERE QuestionID = :questionID")
    Integer getCorrectAnswerIDByQuestionID(Integer questionID);

    @Query("SELECT * FROM Question WHERE TagID = :tagID")
    List<Question> getQuestionsByTagID(Integer tagID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Question...questions);

    @Query("UPDATE question SET TagID = :tagID , Title = :title WHERE QuestionID = :id")
    void updateQuestion(Integer id, Integer tagID, String title);

    @Query("DELETE FROM Question WHERE QuestionID = :questionID")
    void deleteQuestionByID(Integer questionID);

    @Query("UPDATE question SET correctAnswerID = :correctAnswerID WHERE QuestionID = :id")
    void updateQuestionCorrectAnswer(Integer correctAnswerID, Integer id);
}
