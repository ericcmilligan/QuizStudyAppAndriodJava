package com.example.mob_dev_portfolio.Database;

import androidx.room.Database;
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

@Database(entities = {Answer.class, Question.class, Tag.class, Highscore.class}, version = 1)
@TypeConverters({LocalDateTimeConverter.class})
public abstract class QuizDatabase extends RoomDatabase {
    public abstract AnswerDao answerDao();
    public abstract QuestionDao questionDao();
    public abstract TagDao tagDao();
    public abstract HighScoreDao highScoreDao();
    public abstract DatabaseDao databaseDao();
}
