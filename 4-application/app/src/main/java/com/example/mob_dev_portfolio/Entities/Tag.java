package com.example.mob_dev_portfolio.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * An Entity class for a tag within the database.
 */
@Entity(indices = {@Index(value = {"Name"},
        unique = true)})
public class Tag {

    @PrimaryKey(autoGenerate = true)
    private Integer TagID;

    @ColumnInfo(name = "Name")
    private String Name;

    public Tag(String Name) {
        this.Name = Name;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "TagID=" + TagID +
                ", Name='" + Name + '\'' +
                '}';
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getTagID() {
        return TagID;
    }

    public void setTagID(Integer tagID) {
        TagID = tagID;
    }
}
