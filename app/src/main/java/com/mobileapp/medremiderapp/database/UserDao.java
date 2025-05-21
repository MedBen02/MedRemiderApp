package com.mobileapp.medremiderapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mobileapp.medremiderapp.model.User;

import java.util.Date;
import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Update
    int update(User user);

    @Delete
    int delete(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User login(String username, String password);

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    User getUserById(int id);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User getUserByUsername(String username);

    @Query("SELECT * FROM users")
    LiveData<List<User>> getAllUsers();

    @Query("DELETE FROM users")
    void deleteAllUsers();

    @Query("UPDATE users SET name = :name WHERE id = :id")
    void updateName(int id, String name);

    @Query("UPDATE users SET birthDay = :birthDay WHERE id = :id")
    void updateBirthDay(int id, Date birthDay);

    @Query("SELECT * FROM users WHERE name LIKE :name")
    LiveData<List<User>> findUsersByName(String name);

    @Query("SELECT * FROM users WHERE birthDay > :date")
    LiveData<List<User>> getUsersBornAfter(Date date);

    @Query("SELECT * FROM users")
    List<User> getAllUsersImmediate(); // Non-reactive version

}
