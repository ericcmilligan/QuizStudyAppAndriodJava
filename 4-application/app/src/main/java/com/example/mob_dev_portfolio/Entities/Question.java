package com.example.mob_dev_portfolio.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        indices = {@Index(value = {"Title"},
                unique = true)},
        foreignKeys = {@ForeignKey(entity = Tag.class,
        parentColumns = "TagID",
        childColumns = "TagID",
        onDelete = ForeignKey.SET_NULL),
        @ForeignKey(
                entity = Answer.class,
                parentColumns = "AnswerID",
                childColumns = "CorrectAnswerID",
                onDelete = ForeignKey.SET_NULL
        )
})
public class Question implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private Integer QuestionID;

    @ColumnInfo(name = "TagID")
    private Integer TagID;

    @ColumnInfo(name = "CorrectAnswerID")
    private Integer CorrectAnswerID;

    @ColumnInfo(name = "Title")
    private String Title;

    public Question(Integer TagID, Integer CorrectAnswerID, String Title) {
        this.TagID = TagID;
        this.CorrectAnswerID = CorrectAnswerID;
        this.Title = Title;
    }

    protected Question(Parcel in) {
        if (in.readByte() == 0) {
            QuestionID = null;
        } else {
            QuestionID = in.readInt();
        }
        if (in.readByte() == 0) {
            TagID = null;
        } else {
            TagID = in.readInt();
        }
        if (in.readByte() == 0) {
            CorrectAnswerID = null;
        } else {
            CorrectAnswerID = in.readInt();
        }
        Title = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (QuestionID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(QuestionID);
        }
        if (TagID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(TagID);
        }
        if (CorrectAnswerID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(CorrectAnswerID);
        }
        dest.writeString(Title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public String toString() {
        return "Question{" +
                "QuestionID=" + QuestionID +
                ", TagID=" + TagID +
                ", CorrectAnswerID=" + CorrectAnswerID +
                ", Title='" + Title + '\'' +
                '}';
    }

    public Integer getQuestionID() {
        return QuestionID;
    }

    public void setQuestionID(Integer questionID) {
        QuestionID = questionID;
    }

    public Integer getTagID() {
        return TagID;
    }

    public void setTagID(Integer tagID) {
        TagID = tagID;
    }

    public Integer getCorrectAnswerID() {
        return CorrectAnswerID;
    }

    public void setCorrectAnswerID(Integer correctAnswerID) {
        CorrectAnswerID = correctAnswerID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
