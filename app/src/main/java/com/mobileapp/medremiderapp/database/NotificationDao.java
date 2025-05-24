package com.mobileapp.medremiderapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.mobileapp.medremiderapp.model.MedNotification;
import com.mobileapp.medremiderapp.model.DataFlowModels.NotificationWithDetails;

import java.util.List;

@Dao
public interface NotificationDao {
    @Insert
    void insert(MedNotification notification);

    @Update
    void update(MedNotification notification);

    @Delete
    void delete(MedNotification notification);

    @Query("SELECT * FROM med_notifications WHERE reminderId = :reminderId ORDER BY notificationTime ASC")
    LiveData<List<MedNotification>> getNotificationsForReminder(int reminderId);

    @Query("SELECT * FROM med_notifications WHERE " +
            "status = 'SCHEDULED' AND notificationTime <= :endTime")
    LiveData<List<MedNotification>> getPendingNotifications(long endTime);

    @Query("UPDATE med_notifications SET status = :newStatus WHERE id = :notificationId")
    void updateNotificationStatus(int notificationId, String newStatus);

    @Query("DELETE FROM med_notifications WHERE reminderId = :reminderId")
    void deleteNotificationsForReminder(int reminderId);

    @Query("SELECT * FROM med_notifications WHERE " +
            "notificationTime BETWEEN :startDate AND :endDate " +
            "ORDER BY notificationTime ASC")
    LiveData<List<MedNotification>> getNotificationsForDateRange(long startDate, long endDate);

    @Transaction
    @Query("SELECT * FROM med_notifications WHERE " +
            "notificationTime BETWEEN :startDate AND :endDate " +
            "ORDER BY notificationTime ASC")
    LiveData<List<NotificationWithDetails>> getNotificationsWithDetails(long startDate, long endDate);}