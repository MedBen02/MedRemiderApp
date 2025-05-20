package com.mobileapp.medremiderapp.utils;

import androidx.room.TypeConverter;

import java.util.Calendar;
import java.util.Date;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Calendar fromLong(Long value) {
        Calendar cal = Calendar.getInstance();
        if (value != null) {
            cal.setTimeInMillis(value);
        }
        return cal;
    }

    @TypeConverter
    public static Long calendarToLong(Calendar cal) {
        return cal == null ? null : cal.getTimeInMillis();
    }
}