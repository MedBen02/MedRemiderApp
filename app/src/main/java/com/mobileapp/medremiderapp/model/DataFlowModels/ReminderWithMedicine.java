package com.mobileapp.medremiderapp.model.DataFlowModels;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.mobileapp.medremiderapp.model.Medicine;
import com.mobileapp.medremiderapp.model.Reminder;

public class ReminderWithMedicine {
    @Embedded
    public Reminder reminder;

    @Relation(
            parentColumn = "medicineId",
            entityColumn = "id"
    )
    public Medicine medicine;
}