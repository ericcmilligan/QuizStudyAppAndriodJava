package com.example.mob_dev_portfolio.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.example.mob_dev_portfolio.Entities.Tag;

import java.util.List;

@Dao
public interface TagDao {
    @Query("SELECT * FROM Tag")
    List<Tag> getAllTags();

    @Query("SELECT TagID FROM Tag WHERE Name = :tagName")
    Integer getTagIDByName(String tagName);

    @Query("SELECT Name FROM Tag WHERE TagID = :tagID")
    String getTagNameByID(Integer tagID);

    @Query("SELECT * FROM Tag WHERE Name == :tagName")
    Tag checkIfDefaultTagExists(String tagName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Tag...tags);

    @Delete
    void delete(Tag tag);
}
