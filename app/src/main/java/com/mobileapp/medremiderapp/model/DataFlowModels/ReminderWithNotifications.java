package com.mobileapp.medremiderapp.model.DataFlowModels;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.mobileapp.medremiderapp.model.MedNotification;
import com.mobileapp.medremiderapp.model.Reminder;

import java.util.List;

public class ReminderWithNotifications {
    @Embedded
    public Reminder reminder;

    @Relation(
            parentColumn = "id",
            entityColumn = "reminderId"
    )
    public List<MedNotification> notifications;
}