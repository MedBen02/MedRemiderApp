package com.mobileapp.medremiderapp.model;

import static androidx.room.ForeignKey.CASCADE;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Medicines",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = CASCADE))

public class Medicine implements Parcelable {
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

    protected Medicine(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        dose = in.readString();
        stockQuantity = in.readInt();
        userId = in.readInt();
    }

    public static final Creator<Medicine> CREATOR = new Creator<Medicine>() {
        @Override
        public Medicine createFromParcel(Parcel in) {
            return new Medicine(in);
        }

        @Override
        public Medicine[] newArray(int size) {
            return new Medicine[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(dose);
        dest.writeInt(stockQuantity);
        dest.writeInt(userId);
    }
}
