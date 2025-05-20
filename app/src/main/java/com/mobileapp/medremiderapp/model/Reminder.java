package com.mobileapp.medremiderapp.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "reminders",
        foreignKeys = @ForeignKey(entity = Medicine.class,
                parentColumns = "id",
                childColumns = "medicineId",
                onDelete = CASCADE))
public class Reminder {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int frequency;
    private int numberOfUnits;
    private long startDate;
    private long endDate;
    private int medicineId;

    public Reminder() {
    }

    public Reminder(int frequency, int numberOfUnits, long startDate, long endDate, int medicineId) {
        this.frequency = frequency;
        this.numberOfUnits = numberOfUnits;
        this.startDate = startDate;
        this.endDate = endDate;
        this.medicineId = medicineId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfUnits(int numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }
}