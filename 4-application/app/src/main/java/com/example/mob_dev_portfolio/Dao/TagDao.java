package com.example.mob_dev_portfolio.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.mob_dev_portfolio.Entities.Tag;

import java.util.List;

@Dao
public interface TagDao {
    @Query("SELECT * FROM Tag")
    List<Tag> getAllTags();

    @Query("SELECT * FROM Tag WHERE TagID = 1")
    Tag getTagOne();

    @Insert
    void insertAll(Tag...tags);

    @Delete
    void delete(Tag tag);
}
