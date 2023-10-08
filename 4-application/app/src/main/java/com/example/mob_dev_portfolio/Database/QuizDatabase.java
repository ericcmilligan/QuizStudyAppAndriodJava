package com.example.mob_dev_portfolio.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


import com.example.mob_dev_portfolio.Dao.AnswerDao;
import com.example.mob_dev_portfolio.Dao.DatabaseDao;
import com.example.mob_dev_portfolio.Dao.HighScoreDao;
import com.example.mob_dev_portfolio.Dao.QuestionDao;
import com.example.mob_dev_portfolio.Dao.TagDao;
import com.example.mob_dev_portfolio.Entities.Answer;
import com.example.mob_dev_portfolio.Entities.Highscore;
import com.example.mob_dev_portfolio.Entities.Question;
import com.example.mob_dev_portfolio.Entities.Tag;
import com.example.mob_dev_portfolio.TypeConverters.LocalDateTimeConverter;

/**
 * A Room Database for the Quiz App.
 *
 * This class defines the database structure for the Quiz App. It includes tables for storing
 * answers, questions, tags and high scores. Additionally it provides access to various Data
 * Access Objects(DAOs) for performing database operations.
 *
 * @version 1.0
 */

//The main database for this app
@Database(entities = {Answer.class, Question.class, Tag.class, Highscore.class}, version = 1)
@TypeConverters({LocalDateTimeConverter.class})
public abstract class QuizDatabase extends RoomDatabase {

    //Singleton instance of the database
    static QuizDatabase instance = null;

    /**
     * Get the Data Access Object(DAO) for Answer entities
     *
     * @return The Answer DAO
     */
    public abstract AnswerDao answerDao();

    /**
     * Get the Data Access Object(DAO) for Question entities
     *
     * @return The Question DAO
     */
    public abstract QuestionDao questionDao();

    /**
     * Get the Data Access Object(DAO) for Tag entities
     *
     * @return The Tag DAO
     */
    public abstract TagDao tagDao();

    /**
     * Get the Data Access Object(DAO) for High Score entities
     *
     * @return The High Score DAO
     */
    public abstract HighScoreDao highScoreDao();

    /**
     * Get the Data Access Object(DAO) for general database opeations
     *
     * @return The Database DAO
     */
    public abstract DatabaseDao databaseDao();

    //Get a singleton instance of the quiz database
    public static QuizDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context, QuizDatabase.class, "db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
