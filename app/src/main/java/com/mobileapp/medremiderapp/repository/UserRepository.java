package com.mobileapp.medremiderapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.mobileapp.medremiderapp.database.AppDatabase;
import com.mobileapp.medremiderapp.database.UserDao;
import com.mobileapp.medremiderapp.model.User;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {

    private final UserDao userDao;
    private final ExecutorService executorService;

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        userDao = db.userDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(User user, RepositoryCallback<Void> callback) {
        executorService.execute(() -> {
            userDao.insert(user);
            if (callback != null) callback.onComplete(null);
        });
    }

    public void update(User user, RepositoryCallback<Integer> callback) {
        executorService.execute(() -> {
            int rowsUpdated = userDao.update(user);
            if (callback != null) callback.onComplete(rowsUpdated);
        });
    }

    public void delete(User user, RepositoryCallback<Integer> callback) {
        executorService.execute(() -> {
            int rowsDeleted = userDao.delete(user);
            if (callback != null) callback.onComplete(rowsDeleted);
        });
    }

    public void login(String username, String password, RepositoryCallback<User> callback) {
        executorService.execute(() -> {
            User user = userDao.login(username, password);
            if (callback != null) callback.onComplete(user);
        });
    }

    public void getUserById(int id, RepositoryCallback<User> callback) {
        executorService.execute(() -> {
            User user = userDao.getUserById(id);
            if (callback != null) callback.onComplete(user);
        });
    }

    public void updateName(int id, String name, RepositoryCallback<Void> callback) {
        executorService.execute(() -> {
            userDao.updateName(id, name);
            if (callback != null) callback.onComplete(null);
        });
    }

    public void updateBirthDay(int id, Date birthDay, RepositoryCallback<Void> callback) {
        executorService.execute(() -> {
            userDao.updateBirthDay(id, birthDay);
            if (callback != null) callback.onComplete(null);
        });
    }

    public void findUsersByName(String name, RepositoryCallback<LiveData<List<User>>> callback) {
        executorService.execute(() -> {
            LiveData<List<User>> users = userDao.findUsersByName(name);
            if (callback != null) callback.onComplete(users);
        });
    }

    public void getUsersBornAfter(Date date, RepositoryCallback<LiveData<List<User>>> callback) {
        executorService.execute(() -> {
            LiveData<List<User>> users = userDao.getUsersBornAfter(date);
            if (callback != null) callback.onComplete(users);
        });
    }

    public LiveData<List<User>> getAllUsers() {
        return userDao.getAllUsers();
    }

    public List<User> getAllImmediate() {
        return userDao.getAllUsersImmediate();
    }

    public void deleteAllUsers(RepositoryCallback<Void> callback) {
        executorService.execute(() -> {
            userDao.deleteAllUsers();
            if (callback != null) callback.onComplete(null);
        });
    }

    // Callback interface to return results asynchronously
    public interface RepositoryCallback<T> {
        void onComplete(T result);
    }
}
