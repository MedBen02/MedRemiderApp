package com.mobileapp.medremiderapp.model.DataFlowModels;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.mobileapp.medremiderapp.model.MedNotification;
import com.mobileapp.medremiderapp.model.Reminder;

public class NotificationWithDetails {
    @Embedded
    public MedNotification notification;

    @Relation(
            entity = Reminder.class,
            parentColumn = "reminderId",
            entityColumn = "id"
    )
    public ReminderWithMedicine reminderWithMedicine;
}