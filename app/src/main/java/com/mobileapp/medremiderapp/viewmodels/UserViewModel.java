package com.mobileapp.medremiderapp.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.mobileapp.medremiderapp.model.User;
import com.mobileapp.medremiderapp.repository.UserRepository;
import com.google.gson.Gson;

public class UserViewModel extends AndroidViewModel {

    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_USER = "logged_user";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private final UserRepository repository;
    private final SharedPreferences sharedPreferences;
    private final Gson gson = new Gson();

    public MutableLiveData<User> loggedInUser = new MutableLiveData<>();
    public MutableLiveData<Boolean> registrationSuccess = new MutableLiveData<>();
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        sharedPreferences = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void registerUser(User user) {
        repository.getUserByUsername(user.getUsername(), existingUser -> {
            if (existingUser != null) {
                errorMessage.postValue("Username already exists.");
            } else {
                repository.insert(user, result -> {
                    saveUserToPrefs(user); // Save user data on registration
                    registrationSuccess.postValue(true);
                    loggedInUser.postValue(user);
                });
            }
        });
    }

    public void loginUser(String username, String password) {
        repository.login(username, password, user -> {
            if (user != null) {
                saveUserToPrefs(user); // Save user data on login
                loggedInUser.postValue(user);
            } else {
                errorMessage.postValue("Invalid username or password.");
            }
        });
    }

    public void checkLoggedInUser() {
        boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
        if (isLoggedIn) {
            String userJson = sharedPreferences.getString(KEY_USER, null);
            if (userJson != null) {
                User user = gson.fromJson(userJson, User.class);
                loggedInUser.postValue(user);
            }
        }
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        loggedInUser.postValue(null);
    }

    private void saveUserToPrefs(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER, gson.toJson(user));
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    private void clearUserFromPrefs() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USER);
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}