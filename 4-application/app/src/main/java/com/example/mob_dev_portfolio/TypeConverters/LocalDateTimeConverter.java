package com.example.mob_dev_portfolio.TypeConverters;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import java.time.LocalDateTime;

/**
 * A class to convert LocalDateTime objects to and from String format for enabling storage of date
 * within the room database.
 */
public class LocalDateTimeConverter {

    /**
     * Converts a String representation of date and time to a LocalDateTime object.
     *
     * @param dateString The String representation of the date and time (ISO-8601 format)
     * @Return A LocalDateTime object representing the date and time.
     */
    @TypeConverter
    public static LocalDateTime toDate(String dateString) {
        if (dateString == null) {
            return null;
        } else {
            return LocalDateTime.parse(dateString);
        }
    }

    /**
     * Converts a LocalDateTime object to its String representation.
     *
     * @param date The LocalDateTime object to be converted
     * @Return A String representation of the LocalDateTime (ISO-8601 format)
     */
    @TypeConverter
    public static String toDateString(LocalDateTime date) {
        if (date == null) {
            return null;
        } else {
            return date.toString();
        }
    }
}
