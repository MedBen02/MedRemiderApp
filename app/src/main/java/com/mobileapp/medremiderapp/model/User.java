package com.mobileapp.medremiderapp.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String username;
    @NonNull
    private String password;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "birthDay")
    private Date birthDay;

    @Ignore
    public User(@NonNull String username, @NonNull String password) {
        this.username = username;
        this.password = password;
    }

    public User(@NonNull String username, @NonNull String password, String name, Date birthDay) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.birthDay = birthDay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }
}
