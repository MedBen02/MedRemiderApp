package com.mobileapp.medremiderapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.mobileapp.medremiderapp.database.AppDatabase;
import com.mobileapp.medremiderapp.database.UserDao;
import com.mobileapp.medremiderapp.model.User;

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

    public void getUserByUsername(String username, RepositoryCallback<User> callback) {
        executorService.execute(() -> {
            User user = userDao.getUserByUsername(username);
            if (callback != null) callback.onComplete(user);
        });
    }

    public LiveData<List<User>> getAllUsers() {
        // You may want to change DAO method to return LiveData<List<User>> for this to work reactively
        return null; // Or implement a synchronous method with a callback if you want
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
