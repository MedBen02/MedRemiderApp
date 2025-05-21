package com.mobileapp.medremiderapp.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Medicines",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = CASCADE))

public class Medicine {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private String dose;
    private int stockQuantity;
    private int userId;

    public Medicine() {
    }

    public Medicine(String name, String description, String dose, int stockQuantity, int userId) {
        this.name = name;
        this.description = description;
        this.dose = dose;
        this.stockQuantity = stockQuantity;
        this.userId = userId;
    }

    public int getId() {return id;}

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getUserId() {return userId;}

    public void setUserId(int userId) {this.userId = userId;}
}
