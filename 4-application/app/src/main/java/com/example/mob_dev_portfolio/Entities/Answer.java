package com.example.mob_dev_portfolio.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = Question.class,
        parentColumns = "QuestionID",
        childColumns = "QuestionID",
        onDelete = ForeignKey.SET_NULL)
})
public class Answer {
    @PrimaryKey(autoGenerate = true)
    private Integer AnswerID;

    @ColumnInfo(name = "QuestionID")
    private Integer QuestionID;

    @ColumnInfo(name = "Text")
    private String Text;

    public Answer(Integer QuestionID, String Text) {
        this.QuestionID = QuestionID;
        this.Text = Text;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "AnswerID=" + AnswerID +
                ", QuestionID=" + QuestionID +
                ", Text='" + Text + '\'' +
                '}';
    }

    public Integer getAnswerID() {
        return AnswerID;
    }

    public void setAnswerID(Integer answerID) {
        AnswerID = answerID;
    }

    public Integer getQuestionID() {
        return QuestionID;
    }

    public void setQuestionID(Integer questionID) {
        QuestionID = questionID;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }
}
