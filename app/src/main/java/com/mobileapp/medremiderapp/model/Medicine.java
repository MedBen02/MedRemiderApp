package com.mobileapp.medremiderapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Medicines")
public class Medicine {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private String dose;
    private int stockQuantity;

    public Medicine() {
    }

    public Medicine(String name, String description, String dose, int stockQuantity) {
        this.name = name;
        this.description = description;
        this.dose = dose;
        this.stockQuantity = stockQuantity;
    }

    public int getId() {
        return id;
    }

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
}
