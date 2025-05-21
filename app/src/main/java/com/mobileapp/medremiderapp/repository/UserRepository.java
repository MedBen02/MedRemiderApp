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
            try {
                userDao.insert(user);
                if (callback != null) callback.onComplete(null);
            } catch (Exception e) {
                if (callback != null) callback.onError(e);
            }
        });
    }

    public void update(User user, RepositoryCallback<Integer> callback) {
        executorService.execute(() -> {
            try {
                int rowsUpdated = userDao.update(user);
                if (callback != null) callback.onComplete(rowsUpdated);
            } catch (Exception e) {
                if (callback != null) callback.onError(e);
            }
        });
    }

    public void delete(User user, RepositoryCallback<Integer> callback) {
        executorService.execute(() -> {
            try {
                int rowsDeleted = userDao.delete(user);
                if (callback != null) callback.onComplete(rowsDeleted);
            } catch (Exception e) {
                if (callback != null) callback.onError(e);
            }
        });
    }

    public void login(String username, String password, RepositoryCallback<User> callback) {
        executorService.execute(() -> {
            try {
                User user = userDao.login(username, password);
                if (callback != null) callback.onComplete(user);
            } catch (Exception e) {
                if (callback != null) callback.onError(e);
            }
        });
    }

    public void getUserById(int id, RepositoryCallback<User> callback) {
        executorService.execute(() -> {
            try {
                User user = userDao.getUserById(id);
                if (callback != null) callback.onComplete(user);
            } catch (Exception e) {
                if (callback != null) callback.onError(e);
            }
        });
    }

    public void updateName(int id, String name, RepositoryCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                userDao.updateName(id, name);
                if (callback != null) callback.onComplete(null);
            } catch (Exception e) {
                if (callback != null) callback.onError(e);
            }
        });
    }

    public void updateBirthDay(int id, Date birthDay, RepositoryCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                userDao.updateBirthDay(id, birthDay);
                if (callback != null) callback.onComplete(null);
            } catch (Exception e) {
                if (callback != null) callback.onError(e);
            }
        });
    }

    // These return LiveData directly — no need to wrap in background thread
    public LiveData<List<User>> findUsersByName(String name) {
        return userDao.findUsersByName(name);
    }

    public LiveData<List<User>> getUsersBornAfter(Date date) {
        return userDao.getUsersBornAfter(date);
    }

    public LiveData<List<User>> getAllUsers() {
        return userDao.getAllUsers();
    }

    public List<User> getAllImmediate() {
        return userDao.getAllUsersImmediate();
    }

    public void deleteAllUsers(RepositoryCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                userDao.deleteAllUsers();
                if (callback != null) callback.onComplete(null);
            } catch (Exception e) {
                if (callback != null) callback.onError(e);
            }
        });
    }

    public void getUserByUsername(String username, RepositoryCallback<User> callback) {
        executorService.execute(() -> {
            try {
                User user = userDao.getUserByUsername(username);
                if (callback != null) callback.onComplete(user);
            } catch (Exception e) {
                if (callback != null) callback.onError(e);
            }
        });
    }

    public void shutdown() {
        executorService.shutdown();
    }

    // Enhanced Callback with error handling
    public interface RepositoryCallback<T> {
        void onComplete(T result);
        default void onError(Exception e) {} // Optional to override
    }
}
