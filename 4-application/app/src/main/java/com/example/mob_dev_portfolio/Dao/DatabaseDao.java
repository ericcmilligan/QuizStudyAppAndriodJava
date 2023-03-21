package com.example.mob_dev_portfolio.Dao;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface DatabaseDao {
    @Query("DELETE FROM sqlite_sequence;")
    void resetAutoIncrement();
}
