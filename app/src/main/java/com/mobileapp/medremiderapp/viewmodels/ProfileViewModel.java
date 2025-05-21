package com.mobileapp.medremiderapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.mobileapp.medremiderapp.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<String> profileText = new MutableLiveData<>();
    private final MutableLiveData<User> currentUser = new MutableLiveData<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());

    public LiveData<String> getProfileText() {
        return profileText;
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public void setUserProfile(User user) {
        if (user != null) {
            currentUser.setValue(user);
        }
    }

    public String formatBirthDate(Date birthDate) {
        return birthDate != null ? dateFormat.format(birthDate) : "Not specified";
    }

    public void clearProfile() {
        currentUser.setValue(null);
    }
}