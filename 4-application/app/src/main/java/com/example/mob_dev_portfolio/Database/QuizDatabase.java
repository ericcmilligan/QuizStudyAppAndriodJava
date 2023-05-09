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
 * A class for setting up the quiz app room database.
 */
@Database(entities = {Answer.class, Question.class, Tag.class, Highscore.class}, version = 1)
@TypeConverters({LocalDateTimeConverter.class})
public abstract class QuizDatabase extends RoomDatabase {
    static QuizDatabase instance = null;

    public abstract AnswerDao answerDao();
    public abstract QuestionDao questionDao();
    public abstract TagDao tagDao();
    public abstract HighScoreDao highScoreDao();
    public abstract DatabaseDao databaseDao();

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
