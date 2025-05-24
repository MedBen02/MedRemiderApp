package com.mobileapp.medremiderapp.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "med_notifications",
        foreignKeys = @ForeignKey(entity = Reminder.class,
                parentColumns = "id",
                childColumns = "reminderId",
                onDelete = CASCADE))
public class MedNotification {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private long notificationTime;
    private String status; //"SCHEDULED", "DISMISSED", "TAKEN"
    private int reminderId;

    public static final String STATUS_SCHEDULED = "SCHEDULED";
    public static final String STATUS_TAKEN = "TAKEN";
    public static final String STATUS_DISMISSED = "DISMISSED";

    public MedNotification() {
    }

    public MedNotification(long notificationTime, String status, int reminderId) {
        this.notificationTime = notificationTime;
        this.status = status;
        this.reminderId = reminderId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(long notificationTime) {
        this.notificationTime = notificationTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }
}