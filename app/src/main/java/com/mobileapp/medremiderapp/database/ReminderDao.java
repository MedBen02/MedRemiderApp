package com.mobileapp.medremiderapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mobileapp.medremiderapp.model.Reminder;
import java.util.List;

@Dao
public interface ReminderDao {
    @Insert
    long insert(Reminder reminder);

    @Update
    void update(Reminder reminder);

    @Delete
    void delete(Reminder reminder);

    @Query("SELECT * FROM reminders WHERE medicineId = :medicineId")
    LiveData<List<Reminder>> getRemindersForMedicine(int medicineId);

    @Query("SELECT * FROM reminders WHERE id = :reminderId")
    LiveData<Reminder> getReminderById(int reminderId);

    @Query("SELECT * FROM reminders WHERE " +
            ":currentTime BETWEEN startDate AND endDate")
    LiveData<List<Reminder>> getActiveReminders(long currentTime);

    @Query("DELETE FROM reminders WHERE medicineId = :medicineId")
    void deleteRemindersForMedicine(int medicineId);
}